# Java Playwright Test Framework

This directory contains a comprehensive Java-based test automation framework using Playwright, implementing the Page Object Model (POM) pattern for testing the e-commerce web application.

## 🏗️ Framework Structure

```
tests/java-playwright/
├── pom.xml                           # Maven configuration
├── src/
│   ├── main/java/com/ecommerce/
│   │   ├── pages/                    # Page Object classes
│   │   │   ├── BasePage.java         # Base page with common methods
│   │   │   ├── HomePage.java         # Home page objects and actions
│   │   │   ├── LoginPage.java        # Login page objects and actions
│   │   │   ├── ProductsPage.java     # Products listing page
│   │   │   ├── ProductDetailsPage.java # Product details page
│   │   │   ├── CartPage.java         # Shopping cart page
│   │   │   └── CheckoutPage.java     # Checkout process page
│   │   ├── config/                   # Configuration classes
│   │   └── utils/                    # Utility classes
│   └── test/java/com/ecommerce/
│       └── tests/                    # Test classes
│           ├── BaseTest.java         # Base test class with setup/teardown
│           ├── LoginTests.java       # Login functionality tests
│           ├── HomePageTests.java    # Home page tests
│           └── ...                   # Additional test classes
└── target/                           # Maven build output
```

## 🛠️ Technologies Used

- **Java 11+** - Programming language
- **Maven** - Build and dependency management
- **Playwright for Java** - Browser automation
- **JUnit 5** - Testing framework
- **Allure** - Test reporting
- **SLF4J** - Logging framework

## 🚀 Setup and Installation

### Prerequisites

1. **Java 11 or higher**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **Running E-commerce Application**
   - Ensure the application is running at `http://localhost:3000`
   - Backend API should be available at `http://localhost:5001/api`

### Installation

1. **Navigate to the test directory:**
   ```bash
   cd tests/java-playwright
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

3. **Install Playwright browsers:**
   ```bash
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
   ```

## 🧪 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Tests with Specific Profile
```bash
# Local environment (default)
mvn clean test -Plocal

# CI environment
mvn clean test -Pci
```

### Run Tests with Custom Configuration
```bash
mvn clean test \
  -Dbase.url=http://localhost:3000 \
  -Dapi.url=http://localhost:5001/api \
  -Dheadless=true \
  -Dbrowser=chromium
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginTests
```

### Run Specific Test Method
```bash
mvn test -Dtest=LoginTests#testSuccessfulAdminLogin
```

### Run Tests in Different Browsers
```bash
# Chrome/Chromium (default)
mvn test -Dbrowser=chromium

# Firefox
mvn test -Dbrowser=firefox

# Safari/WebKit
mvn test -Dbrowser=webkit
```

## 📊 Test Reporting

### Generate Allure Report
```bash
mvn allure:report
```

### Serve Allure Report
```bash
mvn allure:serve
```

The report will be available at `http://localhost:port` (port will be displayed in console).

### View Surefire Reports
After running tests, view the Surefire HTML reports at:
```
target/site/surefire-report.html
```

## 🏗️ Page Object Model Structure

### BasePage
Contains common methods used across all pages:
- Element interactions (click, fill, getText)
- Wait methods (waitForElementVisible, waitForPageLoad)
- Navigation helpers
- Screenshot functionality
- Logging capabilities

### Page Objects
Each page object extends `BasePage` and contains:
- **Locators**: Element selectors using `data-testid` attributes
- **Actions**: Methods to interact with page elements
- **Validations**: Methods to verify page state and content
- **Navigation**: Methods to navigate between pages

### Test Classes
Each test class extends `BaseTest` and contains:
- **Setup/Teardown**: Browser and page object initialization
- **Test Methods**: Individual test scenarios
- **Assertions**: JUnit 5 assertions for validation
- **Allure Annotations**: For enhanced reporting

## 🔧 Configuration

### System Properties
Configure tests using system properties:

| Property | Default | Description |
|----------|---------|-------------|
| `base.url` | `http://localhost:3000` | Application base URL |
| `api.url` | `http://localhost:5001/api` | API base URL |
| `headless` | `true` | Run browsers in headless mode |
| `browser` | `chromium` | Browser type (chromium, firefox, webkit) |

### Maven Profiles
- **local**: Default profile for local development
- **ci**: Profile for CI/CD environments

## 📝 Writing Tests

### Test Structure
```java
@Test
@DisplayName("Descriptive test name")
@Description("Detailed test description")
@Severity(SeverityLevel.CRITICAL)
@Story("User Story")
void testMethodName() {
    // Given: Setup test data and navigate to page
    
    // When: Perform actions
    
    // Then: Verify results with assertions
}
```

### Using Page Objects
```java
@Test
void testLogin() {
    // Navigate and interact using page objects
    navigateToLogin()
        .enterEmail("user@test.com")
        .enterPassword("password123")
        .clickLoginButton();
    
    // Assertions
    assertTrue(homePage.isUserLoggedIn());
}
```

### Allure Annotations
```java
@Epic("Authentication")
@Feature("Login")
@Story("User Login")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify user can login with valid credentials")
```

## 🐛 Debugging

### Screenshots
Screenshots are automatically taken on test failures and saved to:
```
screenshots/
```

### Traces
Playwright traces are saved on test failures for debugging:
```
test-results/
```

### Logs
Test execution logs are available in the console and can be configured via SLF4J.

## 🔧 Troubleshooting

### Common Issues

1. **Browser Installation Issues**
   ```bash
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
   ```

2. **Application Not Running**
   - Ensure Docker containers are running
   - Check application is accessible at configured URLs

3. **Test Timeouts**
   - Increase timeout values in BasePage
   - Check application performance

4. **Port Conflicts**
   - Update URLs in configuration
   - Check no other services are using the ports

### Useful Commands

```bash
# Clean and reinstall dependencies
mvn clean install -U

# Run tests with debug logging
mvn test -Dlogback.configurationFile=logback-debug.xml

# Skip tests during build
mvn clean install -DskipTests

# Run tests in parallel
mvn test -Dparallel=methods -DthreadCount=4
```

## 🤝 Contributing

1. Follow the existing code structure and naming conventions
2. Add appropriate Allure annotations for new tests
3. Include data-testid attributes for new UI elements
4. Update this README when adding new features
5. Ensure all tests pass before submitting changes

## 📚 Resources

- [Playwright for Java Documentation](https://playwright.dev/java/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Allure Framework](https://docs.qameta.io/allure/)
- [Maven Documentation](https://maven.apache.org/guides/)
