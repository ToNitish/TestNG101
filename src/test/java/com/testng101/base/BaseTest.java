package com.testng101.base;

import com.testng101.listeners.SeleniumLogListener;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class BaseTest {
    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    @Parameters({"browser", "version", "platform"})
    public void setUp(String browser, String version, String platform) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME");
        String accessKey = System.getenv("LT_ACCESS_KEY");

        // Fallback to provided credentials if environment variables are not set
        if (username == null || username.isEmpty()) {
            username = "NitishKVerma";
        }
        if (accessKey == null || accessKey.isEmpty()) {
            accessKey = "LT_3lMiEiSXPaBiOHKDqRPB1dAgzI04po9STptu6mzu4JYFL5S";
        }

        String hubURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        AbstractDriverOptions<?> options;

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setBrowserVersion(version);
                chromeOptions.setPlatformName(platform);
                chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
                options = chromeOptions;
                break;
            case "microsoftedge":
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setBrowserVersion(version);
                edgeOptions.setPlatformName(platform);
                edgeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
                options = edgeOptions;
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setBrowserVersion(version);
                firefoxOptions.setPlatformName(platform);
                firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
                options = firefoxOptions;
                break;
            case "internet explorer":
            case "ie":
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.setBrowserVersion(version);
                ieOptions.setPlatformName(platform);
                ieOptions.ignoreZoomSettings();
                ieOptions.introduceFlakinessByIgnoringSecurityDomains();
                ieOptions.requireWindowFocus(); // Critical for IE 11 popup alerts stability
                ieOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
                options = ieOptions;
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("project", "TestNG 101 Certification");
        ltOptions.put("build", "TestNG 101 Certification Build");
        ltOptions.put("name", "TestScenario - " + browser + " - " + platform);
        ltOptions.put("w3c", true);
        ltOptions.put("video", true);
        ltOptions.put("network", true);
        ltOptions.put("console", true);
        ltOptions.put("visual", true);

        options.setCapability("LT:Options", ltOptions);

        RemoteWebDriver rawDriver = new RemoteWebDriver(new URL(hubURL), options);
        
        // Wrap the raw driver with the event firing decorator
        SeleniumLogListener seleniumListener = new SeleniumLogListener();
        WebDriver decoratedDriver = new EventFiringDecorator<>(seleniumListener).decorate(rawDriver);
        driver.set(decoratedDriver);

        // Log session details
        log.info(">>> LambdaTest Session Created: {} ({}) on {} - Session ID: {}", browser, version, platform, rawDriver.getSessionId());

        // Open the test URL
        getDriver().get("https://www.testmuai.com/selenium-playground/");
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }
}
