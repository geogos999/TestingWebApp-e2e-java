package com.ecommerce.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private final Page page;
    private final String BASE_URL = "http://localhost:3000";

    // Locators
    private final Locator loginLink;
    private final Locator registerLink;
    private final Locator heroSection;
    private final Locator navigationBar;

    public HomePage(Page page) {
        this.page = page;
        this.loginLink = page.locator("text=Login");
        this.registerLink = page.locator("text=Register");
        this.heroSection = page.locator("[data-testid='hero-section']");
        this.navigationBar = page.locator("nav");
    }

    public void navigateToHomePage() {
        logger.info("Navigating to homepage: {}", BASE_URL);
        page.navigate(BASE_URL);
        page.waitForLoadState();
    }

    public boolean isLoaded() {
        try {
            return heroSection.isVisible() && navigationBar.isVisible();
        } catch (Exception e) {
            logger.warn("Error checking if homepage is loaded: {}", e.getMessage());
            return false;
        }
    }

    public void clickLoginLink() {
        logger.info("Clicking login link");
        loginLink.click();
    }

    public void clickRegisterLink() {
        logger.info("Clicking register link");
        registerLink.click();
    }

    public String getPageTitle() {
        return page.title();
    }

    public boolean isNavigationVisible() {
        return navigationBar.isVisible();
    }

    public boolean isHeroSectionVisible() {
        return heroSection.isVisible();
    }
}
