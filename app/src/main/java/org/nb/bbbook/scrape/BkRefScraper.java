package org.nb.bbbook.scrape;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.nb.bbbook.model.TeamMiscStats;
import org.nb.bbbook.model.TeamPerGame;
import org.nb.bbbook.repo.TeamPerGameRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class BkRefScraper {

    private WebDriver driver = ScraperDriver.chromeWebDriver();
    private final ObjectMapper objectMapper = new ObjectMapper();
    TeamPerGameRepository teamPerGameRepository;

    public void parseTeamStats() throws JsonProcessingException {
        driver.navigate().to("https://www.basketball-reference.com/leagues/NBA_2019.html");
        final List<TeamPerGame> teamPerGame = parsePerGameTable(driver.findElement(By.id("div_team-stats-per_game")));
        for (TeamPerGame team : teamPerGame) {
            final String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(team);
//            System.out.println(json);
            teamPerGameRepository.save(team);

        }
        final List<TeamPerGame> oppTeamPerGame = parsePerGameTable(driver.findElement(By.id("opponent-stats-per_game")));
        for (TeamPerGame team : oppTeamPerGame) {
            final String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(team);
//            System.out.println(json);
            teamPerGameRepository.save(team);

        }
        //misc_stats
        parseMiscTeamStatsTable(driver.findElement(By.id("misc_stats")));

        System.out.println();
    }

    private void readSchedule() {
        List<String> months =
        Arrays.asList(new String[] {
            "https://www.basketball-reference.com/leagues/NBA_2019_games-october.html",
            "https://www.basketball-reference.com/leagues/NBA_2019_games-november.html",
            "https://www.basketball-reference.com/leagues/NBA_2019_games-december.html",
            "https://www.basketball-reference.com/leagues/NBA_2019_games-january.html",
            "https://www.basketball-reference.com/leagues/NBA_2019_games-february.html",
            "https://www.basketball-reference.com/leagues/NBA_2019_games-march.html",
            "https://www.basketball-reference.com/leagues/NBA_2019_games-april.html",
        });
        // Implicit wait for javascript DOM updates to happen
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        final List<String> allRows = new ArrayList<>();
        for (String month : months) {
            driver.navigate().to(month);
            final WebElement dropDown = driver.findElement(By.id("all_schedule"))
                .findElement(By.className("hasmore"));
            new Actions(driver).moveToElement(dropDown).build().perform();
            dropDown.findElement(By.xpath("//button[text()[contains(.,'Get table as CSV')]]"))
                .click();
            final WebElement csvSchedule = driver.findElement(By.id("csv_schedule"));
            final BufferedReader bufferedReader = new BufferedReader(new StringReader(csvSchedule.getText()));
            bufferedReader.lines().forEach(l -> allRows.add(l));
        }
        allRows.forEach(l -> System.out.println(l));
    }

    private List<TeamMiscStats> parseMiscTeamStatsTable(WebElement element) {
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(element.getText()));
        final List<String> rows = new ArrayList<>();
        final List<TeamMiscStats> pojos = new ArrayList<>();
        bufferedReader.lines().forEach(l -> rows.add(l));
        for (int i = 2; i < 32; i++) {
            System.out.println(rows.get(i));
            String[] arr = rows.get(i).split("[ ]");
            for (String s : arr) {
                System.out.println("\"" + s + "\"");
            }
            int k = 0;
            final String rank = arr[k];
            String name = arr[++k] + " " + arr[++k];
            if (arr[k+1].matches("^.{2}[^0-9]*$")) {
                name += " " + arr[++k];
            }
            final String age = arr[++k];
            final String w = arr[++k];
            final String l = arr[++k];
            final String pw = arr[++k];
            final String pl = arr[++k];
            final String mov = arr[++k];
            final String sos = arr[++k];
            final String srs = arr[++k];
            final String ortg = arr[++k];
            final String drtg = arr[++k];
            final String pace = arr[++k];
            final String ftr = arr[++k];
            final String threepar = arr[++k];
            final String ts = arr[++k];
            final String efg = arr[++k];
            final String tov = arr[++k];
            final String orb = arr[++k];
            final String ftfga = arr[++k];
            final String oppefga = arr[++k];
            final String opptov = arr[++k];
            final String drb = arr[++k];
            final String oppftfga = arr[++k];
            String arena = arr[++k];
            while (arr[k+1].matches("^[a-zA-Z\\(\\)1]+$")) {
                arena += " " + arr[++k];
                if (arr[k+1].matches("^[0-9][0-9\\,]+")) {
                    break;
                }
            }
            final String attend = arr[++k].replace(",", "");
            final String attendp = arr[++k].replace(",", "");
            pojos.add(new TeamMiscStats(
                rank,
                name,
                age,
                w,
                l,
                pw,
                pl,
                mov,
                sos,
                srs,
                ortg,
                drtg,
                pace,
                ftr,
                threepar,
                ts,
                efg,
                tov,
                orb,
                ftfga,
                oppefga,
                opptov,
                drb,
                oppftfga,
                arena,
                attend,
                attendp
            ));
        }
            System.out.println();

        return pojos;
    }


    private List<TeamPerGame> parsePerGameTable(WebElement perGameTable) {
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(perGameTable.getText()));
        final List<String> rows = new ArrayList<>();
        final List<TeamPerGame> pojos = new ArrayList<>();
        bufferedReader.lines().forEach(l -> rows.add(l));
        for (int i = 1; i < 31; i++) {
            String[] arr = rows.get(i).split("[ ]");
            int k = 0;
            final String rank = arr[k];
            String name = arr[++k] + " " + arr[++k];
            if (arr[k+1].matches("^.{2}[^0-9]*$")) {
                name += " " + arr[++k];
            }
            final String gp = arr[++k];
            final String mp = arr[++k];
            final String fg = arr[++k];
            final String fga = arr[++k];
            final String fgp = arr[++k];
            final String three = arr[++k];
            final String threea = arr[++k];
            final String threep = arr[++k];
            final String two = arr[++k];
            final String twoa = arr[++k];
            final String twop = arr[++k];
            final String ft = arr[++k];
            final String fta = arr[++k];
            final String ftp = arr[++k];
            final String oreb = arr[++k];
            final String dreb = arr[++k];
            final String treb = arr[++k];
            final String ast = arr[++k];
            final String stl = arr[++k];
            final String blk = arr[++k];
            final String tov = arr[++k];
            final String pf = arr[++k];
            final String pts = arr[++k];
            pojos.add(new TeamPerGame(
                rank,
                name,
                gp,
                mp,
                fg,
                fga,
                fgp,
                three,
                threea,
                threep,
                two,
                twoa,
                twop,
                ft,
                fta,
                ftp,
                oreb,
                dreb,
                treb,
                ast,
                stl,
                blk,
                tov,
                pf,
                pts
            ));
        }
        return pojos;
    }

    public static void main(String[] args) throws JsonProcessingException {
        BkRefScraper scraper = new BkRefScraper();
//        scraper.parseTeamStats();
//        scraper.read();
        System.out.println("exit");
    }


}
