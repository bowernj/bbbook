package org.nb.bbbook.scrape;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ScraperDriver {

    public static WebDriver chromeWebDriver() {
        final URL resource = ScraperDriver.class.getResource("chromedriver");
        System.setProperty("webdriver.chrome.driver", resource.getPath());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
        options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
        options.addArguments("--headless"); // only if you are ACTUALLY running headless
        options.addArguments("--no-sandbox"); //https://stackoverflow.com/a/50725918/1689770
        options.addArguments("--disable-infobars"); //https://stackoverflow.com/a/43840128/1689770
        options.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
        options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
        options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        return driver;
    }

//    public static WebDriver getGeckoDriver() {
//        if (geckoDriver == null) {
//            final URL resource = ScraperDriver.class.getResource("geckodriver");
//            System.out.println(resource);
//            System.setProperty("webdriver.gecko.driver", resource.getPath());
//            FirefoxOptions options = new FirefoxOptions();
//            options.addArguments("--headless");
//            DesiredCapabilities desired = new DesiredCapabilities();
////            desired.setCapability("pageLoadStrategy", "eager");
//            options.merge(desired);
//            geckoDriver = new FirefoxDriver(options);
//        }
//        return geckoDriver;
//    }

}
