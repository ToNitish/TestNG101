package com.testng101.tests;

import com.testng101.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class TestScenario1 extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(TestScenario1.class);

    @Test(timeOut = 20000)
    public void testScenario1() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        // 1. Perform an explicit wait till the time all the elements in the DOM are available (body is loaded and visible)
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

        // 2. Use the SoftAssert to validate the Page Title. Validate Against "TestMu AI"
        SoftAssert softAssert = new SoftAssert();
        String actualTitle = getDriver().getTitle();
        log.info("Actual Page Title is: {}", actualTitle);

        softAssert.assertEquals(actualTitle, "TestMu AI", "Page title does not match 'TestMu AI'!");

        // Expecting a failure and proceeding to the following statement
        log.info("Statement after soft assert execution. Continuing test...");

        // Call assertAll at the end of the test to report failures
        softAssert.assertAll();
    }
}
