package org.nb.bbbook.scrape;

import java.util.Optional;
import org.nb.bbbook.model.BoxScore;
import org.nb.bbbook.util.Convert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoxScoreScraper implements Scraper {

    @Autowired
    ScraperRunner scraperRunner;

    @Override
    public Optional<BoxScore> scrape(final WebDriver driver, final String url) {
        try {
            System.out.println(String.format("%s: BoxScoreScraper (%s)", driver.toString(), url));
            driver.navigate().to(url);
            final String pageTitle = driver.getTitle();
            if (pageTitle.toLowerCase().contains("Page Not Found") || pageTitle.toLowerCase().contains("404")) {
                System.out.println(String.format("Page Not Found: (%s)", url));
                return Optional.empty();
            }
            // Line Score Table
            final String score0 = driver.findElement(By.xpath("//*[@id=\"line_score\"]/tbody/tr[1]")).getText();
            System.out.println(score0);
            final String score1 = driver.findElement(By.xpath("//*[@id=\"line_score\"]/tbody/tr[2]")).getText();
            System.out.println(score1);
            final String score2 = driver.findElement(By.xpath("//*[@id=\"line_score\"]/tbody/tr[3]")).getText();
            System.out.println(score2);

            // Four Factors Table
            final String ff1 = driver.findElement(By.xpath("//*[@id=\"four_factors\"]/tbody/tr[2]")).getText();
            System.out.println(ff1);
            final String ff2 = driver.findElement(By.xpath("//*[@id=\"four_factors\"]/tbody/tr[3]")).getText();
            System.out.println(ff2);

            return Optional.of(textToBoxScore(score0, score1, score2, ff1, ff2, Convert.boxScoreUrlToId(url)));

        } catch (Exception e) {
            onFailure(Convert.boxScoreUrlToId(url));
        }

        return Optional.empty();
    }

    @Override
    public void onFailure(String val) {
        System.out.println(String.format("BoxScoreScraper (%s) failed. Adding to retry queue.", val));
        scraperRunner.scrapeBoxScore(val, true);
    }

    private static BoxScore textToBoxScore(String score0, String score1, String score2, String ff1, String ff2, String id) {
        String[] score0tokens = score0.split("[ ]");

        // Get the number of OTs
        int ots = 0;
        for (int i = 0; i < score0tokens.length; i++) {
            if (score0tokens[i].contains("OT")) ots++;
        }

        String[] score1tokens = score1.split("[ ]");
        int j = 0;
        final String visitorTeam = score1tokens[j];
        final String visitorQ1 = score1tokens[++j];
        final String visitorQ2 = score1tokens[++j];
        final String visitorQ3 = score1tokens[++j];
        final String visitorQ4 = score1tokens[++j];
        final String visitorT = score1tokens[++j + ots];

        String[] score2tokens = score2.split("[ ]");
        j = 0;
        final String homeTeam = score2tokens[j];
        final String homeQ1 = score2tokens[++j];
        final String homeQ2 = score2tokens[++j];
        final String homeQ3 = score2tokens[++j];
        final String homeQ4 = score2tokens[++j];
        final String homeT = score2tokens[++j + ots];

        String[] ff1tokens = ff1.split("[ ]");
        j = 1;
        final String pace = ff1tokens[j];

        final String visitorEfg = ff1tokens[++j];
        final String visitorTov = ff1tokens[++j];
        final String visitorOrb = ff1tokens[++j];
        final String visitorFtFga = ff1tokens[++j];

        String[] ff2tokens = ff2.split("[ ]");
        j = 1;
        final String homeEfg = ff2tokens[++j];
        final String homeTov = ff2tokens[++j];
        final String homeOrb = ff2tokens[++j];
        final String homeFtFga = ff2tokens[++j];

        System.out.println();
        return new BoxScore(
            id,
            visitorTeam,
            homeTeam,
            Integer.valueOf(visitorQ1),
            Integer.valueOf(visitorQ2),
            Integer.valueOf(visitorQ3),
            Integer.valueOf(visitorQ4),
            Integer.valueOf(visitorT),
            Integer.valueOf(homeQ1),
            Integer.valueOf(homeQ2),
            Integer.valueOf(homeQ3),
            Integer.valueOf(homeQ4),
            Integer.valueOf(homeT),
            Float.valueOf(pace),
            Float.valueOf(visitorEfg),
            Float.valueOf(visitorTov),
            Float.valueOf(visitorOrb),
            Float.valueOf(visitorFtFga),
            Float.valueOf(homeEfg),
            Float.valueOf(homeTov),
            Float.valueOf(homeOrb),
            Float.valueOf(homeFtFga)
        );

    }
}
