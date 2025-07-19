package com.ecommerce.stepDefinitions;

import com.ecommerce.pageObjects.HomePage;
import com.ecommerce.pageObjects.LoginPage;
import com.ecommerce.utils.DriverManager;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginSteps {

    private final DriverManager driverManager;
    private final HomePage homePage;
    private final LoginPage loginPage;
    private WebDriver driver;

    public LoginSteps(DriverManager driverManager) {
        this.driverManager = driverManager;
        this.driver = driverManager.getDriver();
        this.homePage = new HomePage(driver, driverManager.getWait());
        this.loginPage = new LoginPage(driver, driverManager.getWait());
    }

    @Given("I am on the e-commerce homepage")
    public void i_am_on_the_ecommerce_homepage() {
        homePage.navigateToHomePage();
        Assertions.assertTrue(homePage.isLoaded(), "Homepage should be loaded");
    }

    @When("I navigate to the login page")
    public void i_navigate_to_the_login_page() {
        homePage.clickLoginLink();
        Assertions.assertTrue(loginPage.isLoaded(), "Login page should be loaded");
    }

    @When("I enter valid username {string}")
    public void i_enter_valid_username(String username) {
        loginPage.enterUsername(username);
    }

    @When("I enter valid password {string}")
    public void i_enter_valid_password(String password) {
        loginPage.enterPassword(password);
    }

    @When("I enter invalid username {string}")
    public void i_enter_invalid_username(String username) {
        loginPage.enterUsername(username);
    }

    @When("I enter invalid password {string}")
    public void i_enter_invalid_password(String password) {
        loginPage.enterPassword(password);
    }

    @When("I enter username {string}")
    public void i_enter_username(String username) {
        loginPage.enterUsername(username);
    }

    @When("I enter password {string}")
    public void i_enter_password(String password) {
        loginPage.enterPassword(password);
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        Assertions.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
    }

    @Then("I should see the user dashboard")
    public void i_should_see_the_user_dashboard() {
        try {
            WebElement userDashboard = driver.findElement(By.cssSelector("[data-testid='user-dashboard']"));
            Assertions.assertTrue(userDashboard.isDisplayed(), "User dashboard should be visible");
        } catch (Exception e) {
            Assertions.fail("User dashboard should be visible but was not found");
        }
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        String actualMessage = loginPage.getErrorMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage),
                "Expected error message: " + expectedMessage + ", but got: " + actualMessage);
    }

    @Then("I should remain on the login page")
    public void i_should_remain_on_the_login_page() {
        Assertions.assertTrue(loginPage.isLoaded(), "Should remain on login page");
    }

    @Then("I should see {string}")
    public void i_should_see(String expectedResult) {
        if (expectedResult.contains("dashboard")) {
            if (expectedResult.contains("user")) {
                try {
                    WebElement userDashboard = driver.findElement(By.cssSelector("[data-testid='user-dashboard']"));
                    Assertions.assertTrue(userDashboard.isDisplayed(), "User dashboard should be visible");
                } catch (Exception e) {
                    Assertions.fail("User dashboard should be visible but was not found");
                }
            } else if (expectedResult.contains("admin")) {
                try {
                    WebElement adminDashboard = driver.findElement(By.cssSelector("[data-testid='admin-dashboard']"));
                    Assertions.assertTrue(adminDashboard.isDisplayed(), "Admin dashboard should be visible");
                } catch (Exception e) {
                    Assertions.fail("Admin dashboard should be visible but was not found");
                }
            }
        } else {
            // It's an error message
            String actualMessage = loginPage.getErrorMessage();
            Assertions.assertTrue(actualMessage.contains(expectedResult),
                    "Expected: " + expectedResult + ", but got: " + actualMessage);
        }
    }
}
