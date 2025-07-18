# Java Cucumber E2E Web Testing Framework

## Overview
This project is an automated end-to-end (E2E) testing framework for a web application, built with Java, Cucumber, and Selenium WebDriver. It uses the Page Object Model (POM) design pattern for maintainable and scalable test automation.

## Project Structure
```
├── pom.xml
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── ecommerce/
        │           ├── hooks/
        │           │   └── Hooks.java
        │           ├── pageObjects/
        │           │   ├── HomePage.java
        │           │   └── LoginPage.java
        │           ├── runners/
        │           │   └── TestRunner.java
        │           ├── stepDefinitions/
        │           │   └── LoginSteps.java
        │           └── utils/
        │               └── WebDriverManager.java
        └── resources/
            ├── config/
            └── features/
                ├── login.feature
                └── products.feature
```

## Key Components
- **Feature Files**: Located in `src/test/resources/features/`, written in Gherkin syntax to describe test scenarios.
- **Step Definitions**: Java classes in `stepDefinitions/` that map Gherkin steps to executable code.
- **Page Objects**: Classes in `pageObjects/` encapsulate web page elements and actions.
- **Hooks**: `Hooks.java` manages setup and teardown for each scenario, including driver initialization, screenshot capture on failure, and resource cleanup.
- **Test Runner**: `TestRunner.java` configures and runs Cucumber tests.

## How It Works
1. **Test Execution**: The test runner executes scenarios defined in feature files.
2. **Setup**: Before each scenario, the `Hooks` class initializes the WebDriver.
3. **Step Execution**: Step definitions interact with page objects to perform actions and assertions.
4. **Teardown**: After each scenario, the `Hooks` class handles cleanup, captures screenshots on failure, and quits the driver.

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven
- ChromeDriver or other WebDriver binaries (ensure they are available in your PATH)

### Installation
1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd <project-directory>
   ```
2. Install dependencies:
   ```sh
   mvn clean install
   ```

### Running Tests
Run all tests using Maven:
```sh
mvn test
```

## Customization
- Add new feature files in `src/test/resources/features/`.
- Implement corresponding step definitions in `stepDefinitions/`.
- Add or update page objects in `pageObjects/` as needed.

## Reporting
Test results and reports can be configured via Cucumber plugins in the test runner.

## License
This project is licensed under the MIT License.

