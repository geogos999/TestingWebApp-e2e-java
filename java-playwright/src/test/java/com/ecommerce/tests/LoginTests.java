package com.ecommerce.tests;

import org.junit.jupiter.api.*;
import io.qameta.allure.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for login functionality.
 * Tests various login scenarios including valid/invalid credentials, form validation, etc.
 */
@Epic("Authentication")
@Feature("Login")
public class LoginTests extends BaseTest {
    
    @BeforeEach
    void setupLoginTests() {
        // Navigate to login page before each test
        navigateToLogin();
    }
    
    @Test
    @DisplayName("Successful login with valid admin credentials")
    @Description("Verify that admin user can login successfully with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Admin Login")
    void testSuccessfulAdminLogin() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // When: User logs in with admin credentials
        loginPage.loginAsAdmin();
        loginPage.waitForLoginToComplete();
        
        // Then: User should be redirected to home page and logged in
        assertTrue(isOnHomePage(), "User should be redirected to home page");
        assertTrue(homePage.isUserLoggedIn(), "User should be logged in");
    }
    
    @Test
    @DisplayName("Successful login with valid user credentials")
    @Description("Verify that regular user can login successfully with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Login")
    void testSuccessfulUserLogin() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // When: User logs in with test user credentials
        loginPage.loginAsTestUser();
        loginPage.waitForLoginToComplete();
        
        // Then: User should be redirected to home page and logged in
        assertTrue(isOnHomePage(), "User should be redirected to home page");
        assertTrue(homePage.isUserLoggedIn(), "User should be logged in");
    }
    
    @Test
    @DisplayName("Login fails with invalid credentials")
    @Description("Verify that login fails when using invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    @Story("Login Validation")
    void testLoginWithInvalidCredentials() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // When: User attempts login with invalid credentials
        loginPage.loginWithInvalidCredentials();
        loginPage.waitForLoginError();
        
        // Then: Error message should be displayed and user remains on login page
        assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible");
        assertTrue(loginPage.errorMessageContains("invalid"), "Error message should indicate invalid credentials");
        assertTrue(isOnLoginPage(), "User should remain on login page");
    }
    
    @Test
    @DisplayName("Login form validation - empty fields")
    @Description("Verify that login form shows validation errors for empty fields")
    @Severity(SeverityLevel.NORMAL)
    @Story("Form Validation")
    void testLoginFormValidationEmptyFields() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // When: User attempts login with empty fields
        loginPage.clickLoginButton();
        
        // Then: Login button should remain enabled and form should show validation
        assertTrue(loginPage.isLoginButtonEnabled(), "Login button should remain enabled");
        assertTrue(isOnLoginPage(), "User should remain on login page");
    }
    
    @Test
    @DisplayName("Login form validation - empty email")
    @Description("Verify that login form validates empty email field")
    @Severity(SeverityLevel.NORMAL)
    @Story("Form Validation")
    void testLoginFormValidationEmptyEmail() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // When: User enters only password and attempts login
        loginPage.enterPassword("password123")
                 .clickLoginButton();
        
        // Then: User should remain on login page
        assertTrue(isOnLoginPage(), "User should remain on login page");
    }
    
    @Test
    @DisplayName("Login form validation - empty password")
    @Description("Verify that login form validates empty password field")
    @Severity(SeverityLevel.NORMAL)
    @Story("Form Validation")
    void testLoginFormValidationEmptyPassword() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // When: User enters only email and attempts login
        loginPage.enterEmail("test@example.com")
                 .clickLoginButton();
        
        // Then: User should remain on login page
        assertTrue(isOnLoginPage(), "User should remain on login page");
    }
    
    @Test
    @DisplayName("Navigation to register page")
    @Description("Verify that user can navigate to register page from login page")
    @Severity(SeverityLevel.MINOR)
    @Story("Navigation")
    void testNavigationToRegisterPage() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        assertTrue(loginPage.isRegisterLinkVisible(), "Register link should be visible");
        
        // When: User clicks register link
        loginPage.clickRegisterLink();
        
        // Then: User should be navigated to register page
        assertTrue(getCurrentUrl().contains("register"), "User should be on register page");
    }
    
    @Test
    @DisplayName("Navigation to forgot password page")
    @Description("Verify that user can navigate to forgot password page from login page")
    @Severity(SeverityLevel.MINOR)
    @Story("Navigation")
    void testNavigationToForgotPasswordPage() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        assertTrue(loginPage.isForgotPasswordLinkVisible(), "Forgot password link should be visible");
        
        // When: User clicks forgot password link
        loginPage.clickForgotPasswordLink();
        
        // Then: User should be navigated to forgot password page
        assertTrue(getCurrentUrl().contains("forgot"), "User should be on forgot password page");
    }
    
    @Test
    @DisplayName("Login form field interactions")
    @Description("Verify that login form fields work correctly")
    @Severity(SeverityLevel.MINOR)
    @Story("Form Interaction")
    void testLoginFormFieldInteractions() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // When: User interacts with form fields
        String testEmail = "test@example.com";
        String testPassword = "testpassword";
        
        loginPage.enterEmail(testEmail)
                 .enterPassword(testPassword);
        
        // Then: Fields should contain entered values
        assertEquals(testEmail, loginPage.getEmailFieldValue(), "Email field should contain entered value");
        assertEquals(testPassword, loginPage.getPasswordFieldValue(), "Password field should contain entered value");
        
        // When: User clears fields
        loginPage.clearEmail()
                 .clearPassword();
        
        // Then: Fields should be empty
        assertTrue(loginPage.getEmailFieldValue().isEmpty(), "Email field should be empty after clearing");
        assertTrue(loginPage.getPasswordFieldValue().isEmpty(), "Password field should be empty after clearing");
    }
    
    @Test
    @DisplayName("Login page accessibility")
    @Description("Verify that login page elements are accessible and visible")
    @Severity(SeverityLevel.NORMAL)
    @Story("Accessibility")
    void testLoginPageAccessibility() {
        // Given: User is on login page
        assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // Then: All necessary elements should be visible
        assertTrue(loginPage.isEmailFieldVisible(), "Email field should be visible");
        assertTrue(loginPage.isPasswordFieldVisible(), "Password field should be visible");
        assertTrue(loginPage.isLoginButtonEnabled(), "Login button should be enabled");
        assertTrue(loginPage.isRegisterLinkVisible(), "Register link should be visible");
        assertTrue(loginPage.isForgotPasswordLinkVisible(), "Forgot password link should be visible");
    }
}
