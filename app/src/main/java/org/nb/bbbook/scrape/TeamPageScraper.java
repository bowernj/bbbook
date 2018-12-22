package org.nb.bbbook.scrape;

import java.util.Optional;
import org.nb.bbbook.model.TeamMisc;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamPageScraper implements Scraper {

    @Autowired
    ScraperRunner scraperRunner;

    private static final String path = "https://www.basketball-reference.com/teams/%s/2019.html";

    @Override
    public Optional<TeamMisc> scrape(final WebDriver driver, final String team) {
        try {
            final String url = String.format(path, team);
            System.out.println(String.format("%s: TeamPageScraper (%s)", driver.toString(), url));
            driver.navigate().to(url);
            // team misc values
            final String teamMiscVal = driver.findElement(By.xpath("//*[@id=\"team_misc\"]/tbody/tr[1]")).getText();
            // league ranking
            final String teamMiscRank = driver.findElement(By.xpath("//*[@id=\"team_misc\"]/tbody/tr[2]"))
                .getText();

            return Optional.of(textToTeamMisc(teamMiscVal, teamMiscRank));

        } catch (Exception e) {
            onFailure(team);
//        } finally {
//            System.out.println("Closing TeamHomePageScraper.");
//            driver.close();
        }

        return Optional.empty();
    }

    @Override
    public void onFailure(String team) {
        System.out.println(String.format("TeamPageScraper %s failed. Adding to retry queue.", team));
        scraperRunner.scrapeTeamStatsSeason(team);
    }

    private static TeamMisc textToTeamMisc(String vals, String ranks) {
        final String[] val = vals.split("[ ]");
        final String[] rank = ranks.split("[ ]");
        int i = 0;
        int j = 1;
        final TeamMisc teamMisc = new TeamMisc(
            // Values
            Integer.parseInt(val[++i]),
            Integer.parseInt(val[++i]),
            Integer.parseInt(val[++i]),
            Integer.parseInt(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            Float.parseFloat(val[++i]),
            // Ranks
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j]),
            Integer.parseInt(rank[++j])
        );

        return teamMisc;
    }
}
