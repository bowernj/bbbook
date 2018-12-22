package org.nb.bbbook.scrape;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.nb.bbbook.model.Meta;
import org.nb.bbbook.model.Meta.MetaStatus;
import org.nb.bbbook.model.TeamMisc;
import org.nb.bbbook.model.TeamStatsSeason;
import org.nb.bbbook.algo.fourfactor.TeamModel;
import org.nb.bbbook.model.BoxScore;
import org.nb.bbbook.model.LeagueAverage;
import org.nb.bbbook.model.TeamSplits;
import org.nb.bbbook.repo.BoxScoreRepository;
import org.nb.bbbook.repo.GameRepository;
import org.nb.bbbook.repo.LeagueAverageRepository;
import org.nb.bbbook.repo.MetaRepository;
import org.nb.bbbook.repo.TeamSplitsRepository;
import org.nb.bbbook.repo.TeamStatsSeasonRepository;
import org.nb.bbbook.util.Const;
import org.nb.bbbook.util.Convert;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScraperRunner implements AutoCloseable{
    // Repositories
    @Autowired
    TeamStatsSeasonRepository teamStatsSeasonRepository;
    @Autowired
    TeamSplitsRepository teamSplitsRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    MetaRepository metaRepository;
    @Autowired
    BoxScoreRepository boxScoreRepository;
    @Autowired
    LeagueAverageRepository leagueAverageRepository;

    // Scrapers
    @Autowired
    TeamPageScraper teamPageScraper;
    @Autowired
    SplitsScraper splitsScraper;
    @Autowired
    BoxScoreScraper boxScoreScraper;

    private final int MAX_THREADS = 2;
    private final int MAX_DRIVERS = 2;

    private ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
    private ConcurrentLinkedQueue<WebDriver> drivers = new ConcurrentLinkedQueue<>();
    private final List<Future> futures = new ArrayList<>();

    private final ConcurrentLinkedQueue<String> teamStatsRetryQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> splitsRetryQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> boxScoreRetryQueue = new ConcurrentLinkedQueue<>();

    public ScraperRunner() {
        createDrivers();
    }

    //    public ScraperRunner(
//        GameRepository gameRepository,
//        TeamStatsSeasonRepository teamStatsSeasonRepository,
//        TeamSplitsRepository teamSplitsRepository,
//        MetaRepository metaRepository,
//        BoxScoreRepository boxScoreRepository,
//        TeamPageScraper teamPageScraper,
//        SplitsScraper splitsScraper,
//        BoxScoreScraper boxScoreScraper
//    ) {
//        this.gameRepository = gameRepository;
//        this.teamStatsSeasonRepository = teamStatsSeasonRepository;
//        this.metaRepository = metaRepository;
//        this.boxScoreRepository = boxScoreRepository;
//        this.teamPageScraper = teamPageScraper;
//        this.splitsScraper = splitsScraper;
//        this.boxScoreScraper = boxScoreScraper;
//    }

    public void scrapeTeamStatsSeason() {
//        retryScrapeTeamStatsSeason();
        for (String teamName : Const.TEAMS_SHORT) {
            scrapeTeamStatsSeason(teamName);
        }
        futures.add(executorService.submit(() -> {
            updateLeagueAverage();
        }));
    }

//    private void retryScrapeTeamStatsSeason() {
//        if (!teamStatsRetryQueue.isEmpty()) {
//            System.out.println(String.format("Retrying %s failed items.", teamStatsRetryQueue.size()));
//            for (String teamName : teamStatsRetryQueue) {
//                scrapeTeamStatsSeason(teamName);
//            }
//            if (!teamStatsRetryQueue.isEmpty()) {
//                retryScrapeTeamSplits();
//            }
//        }
//    }

    public void scrapeTeamStatsSeason(String teamName) {
        futures.add(executorService.submit(() -> {
            updateMeta(MetaStatus.REFRESHING);
            final WebDriver driver = getDriver();
            final Optional<TeamMisc> teamPage = teamPageScraper.scrape(driver, teamName);
            if (teamPage.isPresent()) {
                teamStatsSeasonRepository.save(new TeamStatsSeason(teamName, teamPage.get()));
            }
            putDriver(driver);
            updateMeta(MetaStatus.OK);
        }));
    }

    public void scrapeTeamSplits() {
//        retryScrapeTeamSplits();
        for (String teamName : Const.TEAMS_SHORT) {
            scrapeTeamSplits(teamName);
        }
    }

//    private void retryScrapeTeamSplits() {
//        if (!splitsRetryQueue.isEmpty()) {
//            System.out.println(String.format("Retrying %s failed items.", splitsRetryQueue.size()));
//            for (String teamName : splitsRetryQueue) {
//                scrapeTeamSplits(teamName);
//            }
//            if (!splitsRetryQueue.isEmpty()) {
//                retryScrapeTeamSplits();
//            }
//        }
//    }

    public void scrapeTeamSplits(String teamName) {
        futures.add(executorService.submit(() -> {
            updateMeta(MetaStatus.REFRESHING);
            final WebDriver driver = getDriver();
            final Optional<TeamSplits> op = splitsScraper.scrape(driver, teamName);
            if (op.isPresent()) {
                teamSplitsRepository.save(op.get());
            }
            putDriver(driver);
            updateMeta(MetaStatus.OK);
        }));
    }

    public void scrapeBoxScores(List<String> ids, boolean force) {
        for (String id: ids) {
            scrapeBoxScore(id, force);
        }
    }

//    private void retryScrapeBoxScores(boolean force) {
//        if (!boxScoreRetryQueue.isEmpty()) {
//            System.out.println(String.format("Retrying %s failed items.", boxScoreRetryQueue.size()));
//            for (String id : boxScoreRetryQueue) {
//                scrapeBoxScore(force, id);
//            }
//            if (!boxScoreRetryQueue.isEmpty()) {
//                retryScrapeBoxScores(force);
//            }
//        }
//    }

    public void scrapeBoxScore(String id, boolean force) {
        futures.add(executorService.submit(() -> {
            // Only continue if this box score isn't already saved.
            if (!boxScoreRepository.findById(id).isPresent() || force) {
                updateMeta(MetaStatus.REFRESHING);
                final String url = Convert.idToboxScoreUrl(id);
                final WebDriver driver = getDriver();
                final Optional<BoxScore> bs = boxScoreScraper.scrape(driver, url);
                if (bs.isPresent()) {
                    boxScoreRepository.save(bs.get());
                }
                putDriver(driver);
                updateMeta(MetaStatus.OK);
            }
        }));
    }

    private WebDriver getDriver() {
        WebDriver driver = null;

        while (null == driver) {
            driver = drivers.poll();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return driver;
    }

    private void putDriver(WebDriver driver) {
        drivers.add(driver);
    }

    private void createDrivers() {
        for (int i = 0; i < MAX_DRIVERS; i++) {
            drivers.add(ScraperDriver.chromeWebDriver());
        }
    }

    private void closeDrivers() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        while (!drivers.isEmpty()) {
            if (futures.isEmpty()) {
                for (int i = 0; i < MAX_DRIVERS; i++) {
                    final WebDriver driver = getDriver();
                    System.out.println(String.format("Closing driver: %s", driver.toString()));
                    driver.close();
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMeta(MetaStatus status) {
        // Update meta
        metaRepository.deleteById(0);
        metaRepository.save(new Meta(0, Instant.now(), status));
    }

    public LeagueAverage updateLeagueAverage() {
        final List<TeamStatsSeason> teamStats = new ArrayList<>();
        teamStatsSeasonRepository.findAll().forEach(s -> teamStats.add(s));
        float minFourFactor = 0f;
        float maxFourFactor = 0f;
        float minNetRating = 0f;
        float maxNetRating = 0f;
        for (TeamStatsSeason t : teamStats) {
            final TeamModel m = TeamModel.fromTeamStatsSeason(t);
            if (m.getFourFactorScore() < minFourFactor) {
                minFourFactor = m.getFourFactorScore();
            }
            if (m.getFourFactorScore() > maxFourFactor) {
                maxFourFactor = m.getFourFactorScore();
            }
            if (m.getNetRating() < minNetRating) {
                minNetRating = m.getNetRating();
            }
            if (m.getNetRating() > maxNetRating) {
                maxNetRating = m.getNetRating();
            }
        }

        leagueAverageRepository.deleteAll();
        final LeagueAverage la =
            new LeagueAverage(0, minFourFactor, maxFourFactor, minNetRating, maxNetRating);
        leagueAverageRepository.save(la);
        System.out.println("Updating League Average");

        return la;
    }


    public List<Future> getFutures() {
        return futures;
    }

    public ConcurrentLinkedQueue<String> getTeamStatsRetryQueue() {
        return teamStatsRetryQueue;
    }

    public ConcurrentLinkedQueue<String> getSplitsRetryQueue() {
        return splitsRetryQueue;
    }

    public ConcurrentLinkedQueue<String> getBoxScoreRetryQueue() {
        return boxScoreRetryQueue;
    }

    @Override
    public void close() throws Exception {
        for (Future f : futures) {
            f.cancel(true);
        }

        for (WebDriver d : drivers) {
            d.close();
        }
    }
}
