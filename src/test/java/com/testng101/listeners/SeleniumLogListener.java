package com.testng101.listeners;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumLogListener implements WebDriverListener {
    private static final Logger log = LoggerFactory.getLogger(SeleniumLogListener.class);

    @Override
    public void beforeClick(WebElement element) {
        log.info("Attempting to click on element: {}", getElementName(element));
    }

    @Override
    public void afterClick(WebElement element) {
        log.info("Successfully clicked on element.");
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        String keys = (keysToSend != null) ? String.join("", keysToSend) : "";
        log.info("Entering text '{}' into element: {}", keys, getElementName(element));
    }

    private String getElementName(WebElement element) {
        if (element == null) {
            return "null element";
        }
        try {
            String text = element.getText();
            if (text != null && !text.trim().isEmpty()) {
                return "'" + text.trim() + "'";
            }
            String id = element.getAttribute("id");
            if (id != null && !id.trim().isEmpty()) {
                return "ID: " + id;
            }
            String name = element.getAttribute("name");
            if (name != null && !name.trim().isEmpty()) {
                return "Name: " + name;
            }
            return element.getTagName();
        } catch (Exception e) {
            return "WebElement";
        }
    }
}
