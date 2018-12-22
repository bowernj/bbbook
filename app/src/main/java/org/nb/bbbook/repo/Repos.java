package org.nb.bbbook.repo;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nb.bbbook.model.Court;
import org.nb.bbbook.model.Game;
import org.nb.bbbook.util.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Repos {

    @Autowired
    GameRepository gameRepository;

    private final ZoneId easternZone = ZoneId.of("America/New_York");
    private List<Game> allGames = new ArrayList<>();

    public List<Game> getAllGames() {
        if (allGames.isEmpty()) {
            gameRepository.findAll().forEach(g -> allGames.add(g));
        }
        return allGames;
    }

    public List<Game> getTeamGames(String team, Court court) {
        final List<Game> filter = new ArrayList<>();
        getAllGames().forEach(g -> {
            switch (court) {
                case ROAD: {
                    if (Convert.toShortName(g.getVisitorTeam()).equalsIgnoreCase(team)) {
                        filter.add(g);
                    }
                    break;
                }
                case HOME: {
                    if (Convert.toShortName(g.getHomeTeam()).equalsIgnoreCase(team)) {
                        filter.add(g);
                    }
                    break;
                }
                case ALL: {
                    if (Convert.toShortName(g.getVisitorTeam()).equalsIgnoreCase(team) ||
                        Convert.toShortName(g.getHomeTeam()).equalsIgnoreCase(team)) {
                        filter.add(g);
                    }
                    break;
                }
            }
        });
        Collections.sort(filter);
        return filter;
    }

    public long daysOffSinceLastGame(String team) {
        final LocalDate now = LocalDate.now(easternZone);
        final Game g = getLastGame(team);
        return DAYS.between(g.getDate(), now) - 1;
    }

    /**
     * Get the last n games from a list of games.
     */
    public List<Game> getRecentGames(List<Game> games, int n) {
        Collections.sort(games);
        final int idxOf = games.indexOf(getLastGame(games)) + 1;
        int startIdx = idxOf - n > 0 ? idxOf - n : 0;
        return games.subList(startIdx, idxOf);
    }

    public Game getLastGame(List<Game> games) {
        Collections.sort(games);
        final LocalDate now = LocalDate.now(easternZone);
        Game lastGame = null;
        for (Game g : games) {
            if (g.getDate().isBefore(now)) {
                lastGame = g;
            } else {
                break;
            }
        }
        return lastGame;

    }

    public Game getLastGame(String team) {
        return getLastGame(getTeamGames(team, Court.ALL));
    }

    public List<Game> getRecentGames(int days) {
        final LocalDate now = LocalDate.now(easternZone);
        List<Game> filter = new ArrayList<>();
        for (Game g : getAllGames()) {
            final LocalDate gameDate = g.getDate();
            final long btwn = DAYS.between(gameDate, now);
            if (btwn > 0 && btwn <= days) {
                filter.add(g);
            }
        }

        return filter;
    }

    public List<Game> getGamesByDate(LocalDate date) {
        final List<Game> allGames = getAllGames();
        List<Game> games = new ArrayList<>();
        for (Game g : allGames) {
            final LocalDate gameDate = g.getDate();
            if (gameDate.equals(date)) {
                games.add(g);
            }
        }

        return games;
    }

}
