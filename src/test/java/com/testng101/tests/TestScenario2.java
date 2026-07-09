package com.testng101.tests;

import com.testng101.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestScenario2 extends BaseTest {

    @Test(timeOut = 20000)
    public void testScenario2() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        // 1. Click "Checkbox Demo" using specific XPath matching href
        WebElement checkboxDemoLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, 'checkbox-demo')]")));
        try {
            checkboxDemoLink.click();
        } catch (Exception e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", checkboxDemoLink);
        }

        // Wait for URL transition
        wait.until(ExpectedConditions.urlContains("checkbox-demo"));

        // 2. Click the checkbox under the "Single Checkbox Demo" section (using self-healing locator)
        WebElement singleCheckbox;
        try {
            singleCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("isAgeSelected")));
        } catch (Exception e) {
            // Fallback for page structures where input does not contain the static ID directly
            singleCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@type='checkbox'])[1]")));
        }

        // Locate the parent label element which is the visible click target
        WebElement clickTarget;
        try {
            clickTarget = singleCheckbox.findElement(By.xpath(".."));
        } catch (Exception e) {
            clickTarget = singleCheckbox;
        }

        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", clickTarget);
        
        // Try native click first, fall back to JS click on the click target
        try {
            clickTarget.click();
        } catch (Exception e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", clickTarget);
        }

        // 3. Validate whether this checkbox is "selected".
        Assert.assertTrue(singleCheckbox.isSelected(), "Checkbox is NOT selected after clicking once!");

        // Add a small delay to allow event loop processing
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 4. Now, click the checkbox again and validate whether the checkbox is "unselected".
        try {
            clickTarget.click();
        } catch (Exception e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", clickTarget);
        }
        
        // Wait up to 3 seconds for state change to handle legacy browser lag
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementSelectionStateToBe(singleCheckbox, false));
        } catch (Exception e) {
            // Proceed to verify state directly
        }
        
        Assert.assertFalse(singleCheckbox.isSelected(), "Checkbox is STILL selected after clicking twice!");
    }
}
