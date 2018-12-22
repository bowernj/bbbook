package org.nb.bbbook.util;

import com.google.common.collect.ImmutableMap;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.nb.bbbook.model.Game;

public class Convert {

    private static ImmutableMap<String, String> teamNames;
    private static DecimalFormat df = new DecimalFormat("#.#");

    static {
        teamNames = new ImmutableMap.Builder<String, String>()
            .put("ATL", "Atlanta Hawks")
            .put("BOS", "Boston Celtics")
            .put("BRK", "Brooklyn Nets")
            .put("CHO", "Charlotte Hornets")
            .put("CHI", "Chicago Bulls")
            .put("CLE", "Cleveland Cavaliers")
            .put("DET", "Detroit Pistons")
            .put("IND", "Indiana Pacers")
            .put("MIA", "Miami Heat")
            .put("MIL", "Milwaukee Bucks")
            .put("NYK", "New York Knicks")
            .put("ORL", "Orlando Magic")
            .put("PHI", "Philadelphia 76ers")
            .put("TOR", "Toronto Raptors")
            .put("WAS", "Washington Wizards")
            .put("DAL", "Dallas Mavericks")
            .put("DEN", "Denver Nuggets")
            .put("GSW", "Golden State Warriors")
            .put("HOU", "Houston Rockets")
            .put("LAC", "Los Angeles Clippers")
            .put("LAL", "Los Angeles Lakers")
            .put("MEM", "Memphis Grizzlies")
            .put("MIN", "Minnesota Timberwolves")
            .put("NOP", "New Orleans Pelicans")
            .put("OKC", "Oklahoma City Thunder")
            .put("PHO", "Phoenix Suns")
            .put("POR", "Portland Trail Blazers")
            .put("SAC", "Sacramento Kings")
            .put("SAS", "San Antonio Spurs")
            .put("UTA", "Utah Jazz")
            .build();

        df.setRoundingMode(RoundingMode.CEILING);
    }

    @Nullable
    public static String toShortName(String longName) {
        for (Map.Entry<String, String> e : teamNames.entrySet().asList()) {
            if (e.getValue().equalsIgnoreCase(longName)) {
                return e.getKey();
            }
        }
        return null;
    }

    @Nullable
    public static String toLongName(String shortName) {
        return teamNames.getOrDefault(shortName, null);
    }

    public static String boxScoreUrlToId(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".html"));
    }

    public static String idToboxScoreUrl(String id) {
        return String.format("https://www.basketball-reference.com/boxscores/%s.html", id);
    }

    public static String gameToBoxScoreId(Game g) {
        final LocalDate gameDate = g.getDate();
        return String.format(
            "%s%s%s0%s",
            gameDate.getYear(),
            DateUtil.getMonthLeadingZero(gameDate),
            DateUtil.getDayOfMonthLeadingZero(gameDate),
            Convert.toShortName(g.getHomeTeam()));
    }

    public static List<String> gamesToBoxScoreIds(List<Game> games) {
        List<String> boxScoreIds = new ArrayList<>();
        games.forEach(g -> boxScoreIds.add(gameToBoxScoreId(g)));
        return boxScoreIds;
    }

    public static List<String> idsToBoxScoreUrls(List<String> ids) {
        final String urlPrefix = "https://www.basketball-reference.com/boxscores/";
        final String urlSuffix = ".html";
        final List<String> urls = new ArrayList<>();
        ids.forEach(id -> {
            // Eg. https://www.basketball-reference.com/boxscores/201812030DET.html
            urls.add(urlPrefix + id + urlSuffix);
        });

        return urls;
    }

    public static DecimalFormat decimalFormat() { return df; }

}
