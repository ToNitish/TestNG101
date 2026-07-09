# TestNG Certification Automation Framework

This repository implements a production-ready Java automation framework using Maven and TestNG to execute certification scenarios on the LambdaTest (TestMu AI) Selenium Grid.

## Project Structure

```text
├── pom.xml                   # Maven dependencies and Surefire plugin configuration
├── testng.xml                # Parallel execution configuration for cross-browser testing
├── README.md                 # Project documentation
├── src/
│   └── test/
│       └── java/
│           └── com/
│               └── testng101/
│                   ├── base/
│                   │   └── BaseTest.java        # Driver initialization and configuration using ThreadLocal
│                   └── tests/
│                       ├── TestScenario1.java   # Explicit wait and SoftAssert title validation
│                       ├── TestScenario2.java   # Checkbox interaction, validation, and self-healing locators
│                       └── TestScenario3.java   # JavaScript alert handling and resilient clicking
```

## Setup & Configuration

### Prerequisites
* Java JDK 17 or higher
* Maven 3.6+

### Environment Variables
Configure your LambdaTest credentials via environment variables:
```bash
export LT_USERNAME="your_username"
export LT_ACCESS_KEY="your_access_key"
```
*Note: If these environment variables are not set, the framework will automatically fall back to the credentials provided for this assignment.*

## Executing the Tests

The framework runs the tests in parallel at the **class level** across 4 different browser and platform configurations:
1. Chrome 128.0 on Windows 10
2. Microsoft Edge 127.0 on macOS Ventura
3. Firefox 130.0 on Windows 11
4. Internet Explorer 11 on Windows 10

To execute the suite:
```bash
mvn clean test
```

## Implemented Test Scenarios

### Test Scenario 1
* Performs explicit wait for page elements to load.
* Uses TestNG `SoftAssert` to validate the page title against `"TestMu AI"`.
* Fails at the end using `softAssert.assertAll()`, demonstrating expected soft assertion behavior.

### Test Scenario 2
* Interacts with the Checkbox Demo page.
* Selects the single checkbox, validates it is selected.
* Deselects the checkbox and validates it is unselected.
* Employs robust self-healing locators (parent-element toggling) to guarantee cross-browser execution.

### Test Scenario 3
* Navigates to the JavaScript Alerts page.
* Triggers a simple JS alert.
* Validates alert text is `"I am an alert box!"` and accepts it.
* Configured with click retry loops and `requireWindowFocus` to handle IE 11 remote grid stability.
