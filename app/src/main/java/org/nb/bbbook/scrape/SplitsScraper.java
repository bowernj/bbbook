package org.nb.bbbook.scrape;

import java.util.Optional;
import org.nb.bbbook.model.Splits;
import org.nb.bbbook.model.TeamSplits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SplitsScraper implements Scraper {

    @Autowired
    ScraperRunner scraperRunner;

    private static final String path = "https://www.basketball-reference.com/teams/%s/2019/splits/";

    @Override
    public Optional<TeamSplits> scrape(final WebDriver driver, final String team) {
        try {
            final String url = String.format(path, team);
            System.out.println(String.format("%s: SplitsScraper (%s)", driver.toString(), url));
            driver.navigate().to(url);
            final WebElement tbl = driver.findElement(By.id("team_splits"));
            // totals
            final String totalsText = tbl.findElement(By.xpath("//*[@id=\"team_splits\"]/tbody/tr[2]")).getText();
            // home
            final String homeText = tbl.findElement(By.xpath("//*[@id=\"team_splits\"]/tbody/tr[4]")).getText();
            // road
            final String roadText = tbl.findElement(By.xpath("//*[@id=\"team_splits\"]/tbody/tr[5]")).getText();
            final Splits totals = textToSplit(totalsText);
            final Splits home = textToSplit(homeText);
            final Splits road = textToSplit(roadText);

            return Optional.of(new TeamSplits(team, totals, home, road));

        } catch(Exception e) {
            onFailure(team);
        }

        return Optional.empty();
    }

    @Override
    public void onFailure(String team) {
        System.out.println(String.format("SplitsScraper (%s) failed. Adding to retry queue.", team));
        scraperRunner.scrapeTeamSplits(team);
//        scraperRunner.getSplitsRetryQueue().add(team);
    }

    private static Splits textToSplit(String textRow) {
        final String[] tokens = textRow.split("[ ]");
        int k = (tokens[0].equals("Place")) ? 2 : 1;
        Splits split = new Splits(
            Integer.parseInt(tokens[k]),
            Integer.parseInt(tokens[++k]),
            Integer.parseInt(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k]),
            Float.parseFloat(tokens[++k])
        );

        return split;
    }
}
