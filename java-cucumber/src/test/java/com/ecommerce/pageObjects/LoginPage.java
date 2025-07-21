package com.ecommerce.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class LoginPage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page Factory elements
    @FindBy(css = "#username, [name='username'], [data-testid='username']")
    private WebElement usernameField;

    @FindBy(css = "#password, [name='password'], [data-testid='password']")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit'], [data-testid='login-button']")
    private WebElement loginButton;

    @FindBy(css = ".error, .alert-danger, [data-testid='error-message']")
    private WebElement errorMessage;

    @FindBy(css = "form, [data-testid='login-form']")
    private WebElement loginForm;

    @FindBy(css = ".success, .alert-success, [data-testid='success-message']")
    private WebElement successMessage;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(loginForm));
            return isUsernameFieldVisible() && isPasswordFieldVisible();
        } catch (Exception e) {
            logger.warn("Error checking if login page is loaded: {}", e.getMessage());
            return false;
        }
    }

    private boolean isUsernameFieldVisible() {
        try {
            return usernameField.isDisplayed();
        } catch (Exception e) {
            // Fallback: try to find username field by different locators
            try {
                WebElement fallbackUsername = driver.findElement(By.cssSelector("input[type='text'], input[type='email'], input[name*='user'], input[id*='user']"));
                return fallbackUsername.isDisplayed();
            } catch (Exception fallbackException) {
                return false;
            }
        }
    }

    private boolean isPasswordFieldVisible() {
        try {
            return passwordField.isDisplayed();
        } catch (Exception e) {
            // Fallback: try to find password field by different locators
            try {
                WebElement fallbackPassword = driver.findElement(By.cssSelector("input[type='password']"));
                return fallbackPassword.isDisplayed();
            } catch (Exception fallbackException) {
                return false;
            }
        }
    }

    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(usernameField));
            usernameField.clear();
            usernameField.sendKeys(username);
        } catch (Exception e) {
            logger.error("Error entering username: {}", e.getMessage());
            // Fallback: try to find username field by different locators
            try {
                WebElement fallbackUsername = driver.findElement(By.cssSelector("input[type='text'], input[type='email'], input[name*='user'], input[id*='user']"));
                fallbackUsername.clear();
                fallbackUsername.sendKeys(username);
            } catch (Exception fallbackException) {
                throw new RuntimeException("Unable to enter username", e);
            }
        }
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(passwordField));
            passwordField.clear();
            passwordField.sendKeys(password);
        } catch (Exception e) {
            logger.error("Error entering password: {}", e.getMessage());
            // Fallback: try to find password field by different locators
            try {
                WebElement fallbackPassword = driver.findElement(By.cssSelector("input[type='password']"));
                fallbackPassword.clear();
                fallbackPassword.sendKeys(password);
            } catch (Exception fallbackException) {
                throw new RuntimeException("Unable to enter password", e);
            }
        }
    }

    public void clickLoginButton() {
        logger.info("Clicking login button");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();
            // Wait for any potential navigation or error messages
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error("Error clicking login button: {}", e.getMessage());
            // Fallback: try to find login button by different locators
            try {
                WebElement fallbackButton = driver.findElement(By.cssSelector("button[type='submit'], input[type='submit'], button:contains('Login'), [value='Login']"));
                fallbackButton.click();
                Thread.sleep(1000);
            } catch (Exception fallbackException) {
                throw new RuntimeException("Unable to click login button", e);
            }
        }
    }

    public boolean isLoginSuccessful() {
        try {
            // Check if we're redirected away from login page or see success indicators
            return !driver.getCurrentUrl().contains("/login") ||
                   isSuccessMessageVisible() ||
                   isDashboardVisible();
        } catch (Exception e) {
            logger.warn("Error checking login success: {}", e.getMessage());
            return false;
        }
    }

    private boolean isSuccessMessageVisible() {
        try {
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDashboardVisible() {
        try {
            WebElement dashboard = driver.findElement(By.cssSelector("[data-testid='user-dashboard'], [data-testid='admin-dashboard'], .dashboard, #dashboard"));
            return dashboard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            if (isErrorDisplayed()) {
                return errorMessage.getText();
            }
            return "";
        } catch (Exception e) {
            logger.warn("Error getting error message: {}", e.getMessage());
            // Fallback: try to find error message by different locators
            try {
                WebElement fallbackError = driver.findElement(By.cssSelector(".error, .alert, .message, [class*='error'], [class*='alert']"));
                return fallbackError.getText();
            } catch (Exception fallbackException) {
                return "";
            }
        }
    }

    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            // Fallback: try to find error message by different locators
            try {
                WebElement fallbackError = driver.findElement(By.cssSelector(".error, .alert, .message, [class*='error'], [class*='alert']"));
                return fallbackError.isDisplayed();
            } catch (Exception fallbackException) {
                return false;
            }
        }
    }

    public void clearForm() {
        logger.info("Clearing login form");
        try {
            usernameField.clear();
            passwordField.clear();
        } catch (Exception e) {
            logger.warn("Error clearing form: {}", e.getMessage());
        }
    }

    /**
     * Handles browser alerts if present. Accepts or dismisses based on alert text.
     * Currently auto-accepts Chrome's compromised password alert, but can be extended.
     * @return true if an alert was handled, false otherwise
     */
    public boolean handleAlertIfPresent() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            shortWait.until(ExpectedConditions.alertIsPresent());
            String alertText = driver.switchTo().alert().getText();
            logger.info("Alert detected with text: {}", alertText);
            // Example: Chrome's compromised password alert
            if (alertText != null && alertText.toLowerCase().contains("Change your password")) {
                // The password you just used was found in a data breach. Google Password Manager recommends changing your password now.
                driver.switchTo().alert().accept();
                logger.info("Accepted Chrome compromised password alert.");
                return true;
            } else {
                // Default: accept any other alert
                driver.switchTo().alert().accept();
                logger.info("Accepted generic alert.");
                return true;
            }
        } catch (org.openqa.selenium.TimeoutException te) {
            // No alert present
            return false;
        } catch (Exception e) {
            logger.warn("Exception while handling alert: {}", e.getMessage());
            return false;
        }
    }
}
