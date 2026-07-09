package com.testng101.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class LogListener implements ITestListener {
    private static final Logger log = LoggerFactory.getLogger(LogListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        if (browser == null) {
            browser = "default";
        }
        
        String testName = browser + "_" + result.getMethod().getMethodName();
        MDC.put("testName", testName);
        
        log.info("=== STARTED TEST CASE: {} ===", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("=== PASSED TEST CASE: {} ===", result.getMethod().getMethodName());
        MDC.clear();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("=== FAILED TEST CASE: {} ===", result.getMethod().getMethodName());
        log.error("Failure Reason: ", result.getThrowable());
        MDC.clear();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("=== SKIPPED TEST CASE: {} ===", result.getMethod().getMethodName());
        MDC.clear();
    }
}
