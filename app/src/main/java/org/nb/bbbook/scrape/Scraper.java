package org.nb.bbbook.scrape;

import java.util.Optional;
import org.openqa.selenium.WebDriver;

public interface Scraper {

    Optional<?> scrape(WebDriver driver, String val);

    void onFailure(String val);
}
