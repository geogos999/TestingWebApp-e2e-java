package com.ecommerce.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Login page object containing all elements and actions for the login page.
 * This class implements the Page Object Model pattern for the login functionality.
 */
public class LoginPage extends BasePage {
    
    // Page URL
    private static final String LOGIN_URL = "/login";
    
    // Locators using data-testid attributes
    private static final String EMAIL_INPUT = "[data-testid='email-input']";
    private static final String PASSWORD_INPUT = "[data-testid='password-input']";
    private static final String LOGIN_BUTTON = "[data-testid='login-button']";
    private static final String ERROR_MESSAGE = "[data-testid='error-message']";
    private static final String REGISTER_LINK = "[data-testid='register-link']";
    private static final String FORGOT_PASSWORD_LINK = "[data-testid='forgot-password-link']";
    private static final String LOGIN_FORM = "[data-testid='login-form']";
    
    // Test user credentials
    private static final String ADMIN_EMAIL = "admin@ecommerce.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String TEST_USER_EMAIL = "user@test.com";
    private static final String TEST_USER_PASSWORD = "user123";
    
    public LoginPage(Page page) {
        super(page);
    }
    
    // Navigation
    @Step("Navigate to login page")
    public LoginPage navigate() {
        navigateTo(LOGIN_URL);
        waitForLoginFormToLoad();
        return this;
    }
    
    @Step("Wait for login form to load")
    public LoginPage waitForLoginFormToLoad() {
        waitForElementVisible(LOGIN_FORM);
        waitForElementVisible(EMAIL_INPUT);
        waitForElementVisible(PASSWORD_INPUT);
        waitForElementVisible(LOGIN_BUTTON);
        logger.info("Login form loaded successfully");
        return this;
    }
    
    // Input actions
    @Step("Enter email: {email}")
    public LoginPage enterEmail(String email) {
        fillInput(EMAIL_INPUT, email);
        return this;
    }
    
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        fillInput(PASSWORD_INPUT, password);
        return this;
    }
    
    @Step("Clear email field")
    public LoginPage clearEmail() {
        clearInput(EMAIL_INPUT);
        return this;
    }
    
    @Step("Clear password field")
    public LoginPage clearPassword() {
        clearInput(PASSWORD_INPUT);
        return this;
    }
    
    // Button actions
    @Step("Click login button")
    public LoginPage clickLoginButton() {
        clickElement(LOGIN_BUTTON);
        return this;
    }
    
    @Step("Click register link")
    public void clickRegisterLink() {
        clickElement(REGISTER_LINK);
        logger.info("Clicked register link");
    }
    
    @Step("Click forgot password link")
    public void clickForgotPasswordLink() {
        clickElement(FORGOT_PASSWORD_LINK);
        logger.info("Clicked forgot password link");
    }
    
    // Combined actions
    @Step("Perform login with email: {email}")
    public LoginPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        logger.info("Performed login with email: {}", email);
        return this;
    }
    
    @Step("Login as admin user")
    public LoginPage loginAsAdmin() {
        return login(ADMIN_EMAIL, ADMIN_PASSWORD);
    }
    
    @Step("Login as test user")
    public LoginPage loginAsTestUser() {
        return login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }
    
    @Step("Attempt login with invalid credentials")
    public LoginPage loginWithInvalidCredentials() {
        return login("invalid@email.com", "wrongpassword");
    }
    
    // Validation methods
    @Step("Get error message text")
    public String getErrorMessage() {
        if (isErrorMessageVisible()) {
            return getText(ERROR_MESSAGE);
        }
        return "";
    }
    
    @Step("Check if error message is visible")
    public boolean isErrorMessageVisible() {
        return isElementVisible(ERROR_MESSAGE);
    }
    
    @Step("Check if login button is enabled")
    public boolean isLoginButtonEnabled() {
        return isElementEnabled(LOGIN_BUTTON);
    }
    
    @Step("Check if email field is visible")
    public boolean isEmailFieldVisible() {
        return isElementVisible(EMAIL_INPUT);
    }
    
    @Step("Check if password field is visible")
    public boolean isPasswordFieldVisible() {
        return isElementVisible(PASSWORD_INPUT);
    }
    
    @Step("Check if register link is visible")
    public boolean isRegisterLinkVisible() {
        return isElementVisible(REGISTER_LINK);
    }
    
    @Step("Check if forgot password link is visible")
    public boolean isForgotPasswordLinkVisible() {
        return isElementVisible(FORGOT_PASSWORD_LINK);
    }
    
    @Step("Get email field value")
    public String getEmailFieldValue() {
        return getAttribute(EMAIL_INPUT, "value");
    }
    
    @Step("Get password field value")
    public String getPasswordFieldValue() {
        return getAttribute(PASSWORD_INPUT, "value");
    }
    
    // Validation helpers
    @Step("Verify login form is displayed")
    public boolean isLoginFormDisplayed() {
        return isElementVisible(LOGIN_FORM) &&
               isEmailFieldVisible() &&
               isPasswordFieldVisible() &&
               isLoginButtonEnabled();
    }
    
    @Step("Verify error message contains: {expectedText}")
    public boolean errorMessageContains(String expectedText) {
        String actualMessage = getErrorMessage();
        boolean contains = actualMessage.toLowerCase().contains(expectedText.toLowerCase());
        logger.info("Error message contains '{}': {} (actual: '{}')", expectedText, contains, actualMessage);
        return contains;
    }
    
    @Step("Wait for login to complete")
    public void waitForLoginToComplete() {
        // Wait for URL to change (successful login redirects)
        page.waitForFunction("() => window.location.pathname !== '/login'", 
                           new Page.WaitForFunctionOptions().setTimeout(10000));
        logger.info("Login completed - redirected from login page");
    }
    
    @Step("Wait for login error")
    public LoginPage waitForLoginError() {
        waitForElementVisible(ERROR_MESSAGE, 5000);
        logger.info("Login error message appeared");
        return this;
    }
}
