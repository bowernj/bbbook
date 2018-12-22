package org.nb.bbbook.web;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import javax.annotation.Nullable;
import org.nb.bbbook.algo.TotalPointsModel;
import org.nb.bbbook.algo.fourfactor.TeamModel;
import org.nb.bbbook.model.BoxScore;
import org.nb.bbbook.model.Court;
import org.nb.bbbook.model.CsvConvert;
import org.nb.bbbook.model.Game;
import org.nb.bbbook.model.LeagueAverage;
import org.nb.bbbook.model.Meta;
import org.nb.bbbook.model.Meta.MetaStatus;
import org.nb.bbbook.model.TeamSplits;
import org.nb.bbbook.model.TeamStatsSeason;
import org.nb.bbbook.repo.GameRepository;
import org.nb.bbbook.scrape.ScraperRunner;
import org.nb.bbbook.util.Convert;
import org.nb.bbbook.view.DailySummaryView;
import org.nb.bbbook.view.FourFactorsView;
import org.nb.bbbook.view.GameView;
import org.nb.bbbook.view.GameView.Builder;
import org.nb.bbbook.view.MatchupView;
import org.nb.bbbook.view.ResultsView;
import org.nb.bbbook.view.SeasonView;
import org.nb.bbbook.repo.BoxScoreRepository;
import org.nb.bbbook.repo.LeagueAverageRepository;
import org.nb.bbbook.repo.MetaRepository;
import org.nb.bbbook.repo.Repos;
import org.nb.bbbook.repo.TeamSplitsRepository;
import org.nb.bbbook.repo.TeamStatsSeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {
    // Repositories
    @Autowired
    GameRepository gameRepository;
    @Autowired
    TeamStatsSeasonRepository teamStatsSeasonRepository;
    @Autowired
    TeamSplitsRepository teamSplitsRepository;
    @Autowired
    MetaRepository metaRepository;
    @Autowired
    BoxScoreRepository boxScoreRepository;
    @Autowired
    LeagueAverageRepository leagueAverageRepository;

    @Autowired
    ScraperRunner scraperRunner;

    @Autowired
    Repos repos;

    private final ZoneId easternZone = ZoneId.of("America/New_York");
    private final List<Future> futures = new ArrayList<>();

    public MainService(
        GameRepository gameRepository,
        TeamStatsSeasonRepository teamStatsSeasonRepository,
        TeamSplitsRepository teamSplitsRepository,
        MetaRepository metaRepository,
        BoxScoreRepository boxScoreRepository,
        Repos repos
    ) {
        this.gameRepository = gameRepository;
        this.teamStatsSeasonRepository = teamStatsSeasonRepository;
        this.teamSplitsRepository = teamSplitsRepository;
        this.metaRepository = metaRepository;
        this.boxScoreRepository = boxScoreRepository;
        this.repos = repos;

        // Reload the schedule
        gameRepository.deleteAll();
        final ClassLoader classLoader = getClass().getClassLoader();
        final List<Game> games = CsvConvert
            .read(classLoader.getResource("org/nb/bbbook/data/schedule.csv").getPath());
        games.forEach(g -> gameRepository.save(g));

        // Reset meta status if the App started in a bad state.
        if (getMeta().getStatus() != MetaStatus.OK) {
            metaRepository.deleteById(0);
            metaRepository.save(new Meta(0, Instant.now(), MetaStatus.OK));
        }
    }

    public Meta refreshSeason(boolean force) {
        Meta meta = getMeta();
        if (force || meta.getStatus() != MetaStatus.REFRESHING) {
            for (Future future : scraperRunner.getFutures()) {
                future.cancel(true);
            }

            // Start a scrape of team season stats.
            scraperRunner.scrapeTeamStatsSeason();
            scraperRunner.scrapeTeamSplits();
        }
        return meta;
    }

    public Meta refreshBoxScores(boolean force) {
        Meta meta = getMeta();
        if (force || meta.getStatus() != MetaStatus.REFRESHING) {
            for (Future future : futures) {
                future.cancel(true);
            }
            // Start a scrape of the past 21 days worth of box scores.
            final List<Game> recentGames = repos.getRecentGames(21);
            final List<String> boxScoreIds = new ArrayList<>();
            recentGames.forEach(g -> boxScoreIds.add(Convert.gameToBoxScoreId(g)));
            scraperRunner.scrapeBoxScores(boxScoreIds, force);
        }

        return meta;
    }

    public Meta getMeta() {
        final Optional<Meta> meta = metaRepository.findById(0);
        return meta.orElseGet(() -> new Meta(0, Instant.now(), MetaStatus.BAD));
    }

    private LeagueAverage getLeagueAverage() {
        final Optional<LeagueAverage> la = leagueAverageRepository.findById(0);
        return la.orElseGet(() -> scraperRunner.updateLeagueAverage());
    }

    @Nullable
    public TeamStatsSeason findTeamStats(String key) {
        final Optional<TeamStatsSeason> result = teamStatsSeasonRepository.findById(key);
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public List<TeamStatsSeason> allTeamStats() {
        final Iterable<TeamStatsSeason> result = teamStatsSeasonRepository.findAll();
        final List<TeamStatsSeason> list = new ArrayList<>();
        result.iterator().forEachRemaining(e -> list.add(e));
        return list;
    }

    public SeasonView seasonView() {
        final List<TeamStatsSeason> teams = allTeamStats();
        List<TeamModel> models = new ArrayList<>();
        for (TeamStatsSeason t : teams) {
            models.add(TeamModel.fromTeamStatsSeason(t));
        }
        final LeagueAverage la = getLeagueAverage();
        final List<FourFactorsView> views = new ArrayList<>();
        for (TeamModel model : models) {
            if (la != null) {
                model.scaleScore(la);
            }
            views.add(FourFactorsView.fromModel(model));
        }
        Collections.sort(views);
        return new SeasonView(views);
    }

    public DailySummaryView scheduleView() {
        return scheduleView(LocalDate.now(easternZone));
    }

    public DailySummaryView scheduleView(LocalDate date) {
        final List<Game> todayGames = repos.getGamesByDate(date);
        List<GameView> gameViews = new ArrayList<>();
        todayGames.forEach(g -> {
            final Builder builder = new Builder(g.getDate(), g.getStart(), g.getVisitorTeam(), g.getHomeTeam());
            gameViews.add(builder.build());
        });
        Collections.sort(gameViews);

        return new DailySummaryView(gameViews);
    }

    public Optional<MatchupView> createMatchupView(String visitor, String home, int days) {
        Optional<FourFactorsView> v = null;
        Optional<FourFactorsView> h = null;
        v = createFourFactorsView(visitor, days, Court.ALL);
        h = createFourFactorsView(home, days, Court.ALL);
        if (v.isPresent() && h.isPresent()) {
            return Optional.of(new MatchupView(
                days,
                v.get(),
                h.get()));
        }
        return Optional.empty();
    }

    /**
     * Create a four factors model for a specific team, using data from the past N days.
     */
    public Optional<FourFactorsView> createFourFactorsView(String team, int days, Court court) {
        if (days <= 0) {
            // TODO use whole of season data
        } else {
            // Use past n games.
            final List<Game> allTeamGames = repos.getTeamGames(team, court);
            final List<Game> recentGames = repos.getRecentGames(allTeamGames, days);
            final List<String> boxScoreIds = Convert.gamesToBoxScoreIds(recentGames);
            final List<BoxScore> boxScores = getBoxScores(boxScoreIds);
            if (!boxScores.isEmpty()) {
                final TeamModel model = TeamModel.fromBoxScores(team, boxScores);
                final LeagueAverage la = getLeagueAverage();
                if (la != null) {
                    model.scaleScore(la);
                }
                return Optional.of(FourFactorsView.fromModel(model));
            }
        }

        return Optional.empty();
    }

    private List<BoxScore> getBoxScores(List<String> ids) {
        final List<BoxScore> boxScores = new ArrayList<>();
        final List<String> notPresent = new ArrayList<>();
        for (String id: ids) {
            final Optional<BoxScore> op = boxScoreRepository.findById(id);
            if (op.isPresent()) {
                boxScores.add(op.get());
            } else {
                notPresent.add(id);
            }
        }

        // Scrape for the missing scores
        if (!notPresent.isEmpty()) {
            System.out.println(String.format(
                "Starting a scrape for %s missing box scores.",
                notPresent.size(),
                boxScores.size()));
            scraperRunner.scrapeBoxScores(notPresent, false);
        }

        return boxScores;
    }

    public long daysOffSinceLastGame(String team) {
        return repos.daysOffSinceLastGame(team);
    }

    private Optional<TotalPointsModel> createTotalPointsModel(Game g) {
        final Optional<TeamStatsSeason> vStats =
            teamStatsSeasonRepository.findById(Convert.toShortName(g.getVisitorTeam()));
        final Optional<TeamStatsSeason> hStats =
            teamStatsSeasonRepository.findById(Convert.toShortName(g.getHomeTeam()));
        final Optional<TeamSplits> vSplits =
            teamSplitsRepository.findById(Convert.toShortName(g.getHomeTeam()));
        final Optional<TeamSplits> hSplits =
            teamSplitsRepository.findById(Convert.toShortName(g.getVisitorTeam()));

        if (hStats.isPresent() && vStats.isPresent() && hSplits.isPresent() && vSplits.isPresent()) {
            return Optional.of(new TotalPointsModel(vStats.get(), hStats.get(), vSplits.get(), hSplits.get()));
        }

        // TODO if any are missing, add to scraper queue?

        return Optional.empty();
    }

    public ResultsView getResultsView(String team, Court court, int numGames) {
        final List<Game> teamGames = repos.getTeamGames(team, court);
        final List<Game> recentGames = repos.getRecentGames(teamGames, numGames);
        final List<String> boxScoreIds = Convert.gamesToBoxScoreIds(recentGames);
        final List<BoxScore> boxScores = getBoxScores(boxScoreIds);
        return ResultsView.fromBoxScores(team, boxScores);
    }
}
