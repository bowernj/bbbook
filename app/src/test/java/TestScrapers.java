import org.junit.Test;
import org.nb.bbbook.scrape.SplitsScraper;
import org.nb.bbbook.scrape.TeamPageScraper;

public class TestScrapers {

    @Test
    public void testSplitsScraper() {
        SplitsScraper scraper = new SplitsScraper();
//        scraper.read();
    }

    @Test
    public void testTeamPageScraper() {
        TeamPageScraper scraper = new TeamPageScraper();
//        scraper.readTeamMisc();
    }

    @Test
    public void testBoxScoreScraper() {
//        BoxScoreScraper scraper = new BoxScoreScraper();
//        final Optional<BoxScore> result = scraper
//            .scrape("https://www.basketball-reference.com/boxscores/201812050ORL.html");
//        System.out.println(result.get());
    }
}
