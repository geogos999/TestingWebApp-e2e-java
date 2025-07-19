package com.ecommerce.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String BASE_URL = "http://localhost:3000";

    // Page Factory elements
    @FindBy(xpath = "//a[contains(text(),'Login')] | //button[contains(text(),'Login')]")
    private WebElement loginLink;

    @FindBy(xpath = "//a[contains(text(),'Register')] | //button[contains(text(),'Register')]")
    private WebElement registerLink;

    @FindBy(css = "[data-testid='hero-section']")
    private WebElement heroSection;

    @FindBy(tagName = "nav")
    private WebElement navigationBar;

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void navigateToHomePage() {
        logger.info("Navigating to homepage: {}", BASE_URL);
        driver.get(BASE_URL);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(navigationBar));
            return isHeroSectionVisible() && isNavigationVisible();
        } catch (Exception e) {
            logger.warn("Error checking if homepage is loaded: {}", e.getMessage());
            return false;
        }
    }

    public void clickLoginLink() {
        logger.info("Clicking login link");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginLink));
            loginLink.click();
        } catch (Exception e) {
            logger.error("Error clicking login link: {}", e.getMessage());
            // Fallback: try to find login link by different locators
            try {
                WebElement fallbackLogin = driver.findElement(By.xpath("//a[contains(text(),'Login')] | //button[contains(text(),'Login')] | //*[@data-testid='login-link']"));
                fallbackLogin.click();
            } catch (Exception fallbackException) {
                throw new RuntimeException("Unable to click login link", e);
            }
        }
    }

    public void clickRegisterLink() {
        logger.info("Clicking register link");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(registerLink));
            registerLink.click();
        } catch (Exception e) {
            logger.error("Error clicking register link: {}", e.getMessage());
            throw new RuntimeException("Unable to click register link", e);
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isNavigationVisible() {
        try {
            return navigationBar.isDisplayed();
        } catch (Exception e) {
            logger.warn("Navigation bar not visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isHeroSectionVisible() {
        try {
            return heroSection.isDisplayed();
        } catch (Exception e) {
            logger.warn("Hero section not visible: {}", e.getMessage());
            // Fallback: check if any main content area is visible
            try {
                WebElement mainContent = driver.findElement(By.cssSelector("main, .main-content, [data-testid='main-content']"));
                return mainContent.isDisplayed();
            } catch (Exception fallbackException) {
                return false;
            }
        }
    }
}
