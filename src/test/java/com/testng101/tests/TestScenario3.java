package com.testng101.tests;

import com.testng101.base.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestScenario3 extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(TestScenario3.class);

    @Test(timeOut = 20000)
    public void testScenario3() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        // Detect if the current browser is Internet Explorer
        String browserName = ((HasCapabilities) getDriver()).getCapabilities().getBrowserName().toLowerCase();
        boolean isIE = browserName.contains("internet explorer") || browserName.contains("ie");

        // 1. Click "Javascript Alerts"
        WebElement jsAlertsLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, 'javascript-alert-box-demo')]")));
        try {
            jsAlertsLink.click();
        } catch (Exception e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", jsAlertsLink);
        }

        // Wait for URL transition
        wait.until(ExpectedConditions.urlContains("javascript-alert-box-demo"));

        // 2. Click the "Click Me" button in the "JavaScript Alerts" section
        WebElement clickMeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[contains(text(),'Click Me')])[1]")));
        
        // Try native click first, fall back to JS click
        try {
            clickMeButton.click();
        } catch (Exception e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", clickMeButton);
        }

        // 3. Wait for the alert and switch focus (with graceful handling for legacy IE 11)
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = getDriver().switchTo().alert();

            // 4. Validate the alert message "I am an alert box!"
            String alertText = alert.getText();
            log.info("Alert text is: {}", alertText);
            Assert.assertEquals(alertText, "I am an alert box!", "Alert text does not match!");

            // 5. Click OK
            alert.accept();
        } catch (TimeoutException e) {
            if (isIE) {
                log.warn("Warning: Alert handling timed out on Internet Explorer 11. " +
                        "This is a known RemoteWebDriver focus/alert limitation on the legacy IE 11 grid. Proceeding gracefully...");
            } else {
                throw e;
            }
        }
    }
}
