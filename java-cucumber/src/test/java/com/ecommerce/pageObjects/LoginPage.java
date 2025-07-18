package com.ecommerce.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private final Page page;

    // Locators
    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator errorMessage;
    private final Locator loginForm;
    private final Locator successMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.usernameField = page.locator("#username, [name='username'], [data-testid='username']");
        this.passwordField = page.locator("#password, [name='password'], [data-testid='password']");
        this.loginButton = page.locator("button[type='submit'], button:has-text('Login'), [data-testid='login-button']");
        this.errorMessage = page.locator(".error, .alert-danger, [data-testid='error-message']");
        this.loginForm = page.locator("form, [data-testid='login-form']");
        this.successMessage = page.locator(".success, .alert-success, [data-testid='success-message']");
    }

    public boolean isLoaded() {
        try {
            return loginForm.isVisible() && usernameField.isVisible() && passwordField.isVisible();
        } catch (Exception e) {
            logger.warn("Error checking if login page is loaded: {}", e.getMessage());
            return false;
        }
    }

    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        usernameField.clear();
        usernameField.fill(username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        passwordField.clear();
        passwordField.fill(password);
    }

    public void clickLoginButton() {
        logger.info("Clicking login button");
        loginButton.click();
        page.waitForTimeout(1000); // Wait for any potential navigation or error messages
    }

    public boolean isLoginSuccessful() {
        try {
            // Check if we're redirected away from login page or see success indicators
            return !page.url().contains("/login") ||
                   successMessage.isVisible() ||
                   page.locator("[data-testid='user-dashboard'], [data-testid='admin-dashboard']").isVisible();
        } catch (Exception e) {
            logger.warn("Error checking login success: {}", e.getMessage());
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            if (errorMessage.isVisible()) {
                return errorMessage.textContent();
            }
            return "";
        } catch (Exception e) {
            logger.warn("Error getting error message: {}", e.getMessage());
            return "";
        }
    }

    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void clearForm() {
        logger.info("Clearing login form");
        usernameField.clear();
        passwordField.clear();
    }
}
