package com.ecommerce.tests;

import com.microsoft.playwright.*;
import com.ecommerce.pages.*;
import org.junit.jupiter.api.*;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

/**
 * Base test class containing common setup and teardown for all test classes.
 * This class handles Playwright browser initialization and page object creation.
 */
public abstract class BaseTest {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    // Configuration
    protected static final String BASE_URL = System.getProperty("base.url", "http://localhost:3000");
    protected static final String API_URL = System.getProperty("api.url", "http://localhost:5001/api");
    protected static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "true"));
    protected static final String BROWSER_TYPE = System.getProperty("browser", "chromium");
    
    // Playwright instances
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    
    // Page objects
    protected HomePage homePage;
    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected ProductDetailsPage productDetailsPage;
    protected CartPage cartPage;
    protected CheckoutPage checkoutPage;
    
    @BeforeAll
    static void setupPlaywright() {
        logger.info("Setting up Playwright...");
        playwright = Playwright.create();
        
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(HEADLESS)
                .setSlowMo(50); // Add slight delay for visibility
        
        switch (BROWSER_TYPE.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;
            case "chromium":
            default:
                browser = playwright.chromium().launch(launchOptions);
                break;
        }
        
        logger.info("Browser {} launched successfully", BROWSER_TYPE);
    }
    
    @BeforeEach
    void setupTest() {
        logger.info("Setting up test...");
        
        // Create new browser context for each test
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setLocale("en-US")
                .setTimezoneId("America/New_York");
        
        context = browser.newContext(contextOptions);
        
        // Enable tracing for debugging
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        
        page = context.newPage();
        
        // Initialize page objects
        initializePageObjects();
        
        logger.info("Test setup completed");
    }
    
    @AfterEach
    void teardownTest(TestInfo testInfo) {
        logger.info("Tearing down test: {}", testInfo.getDisplayName());
        
        try {
            // Save trace if test failed
            if (testInfo.getTags().contains("failed")) {
                String tracePath = "test-results/" + testInfo.getDisplayName() + "_trace.zip";
                context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(tracePath)));
                logger.info("Trace saved to: {}", tracePath);
            } else {
                context.tracing().stop();
            }
            
            // Take screenshot on failure
            if (testInfo.getTags().contains("failed")) {
                takeScreenshot(testInfo.getDisplayName() + "_failure");
            }
            
        } catch (Exception e) {
            logger.warn("Error during test teardown: {}", e.getMessage());
        } finally {
            // Close context
            if (context != null) {
                context.close();
            }
        }
    }
    
    @AfterAll
    static void teardownPlaywright() {
        logger.info("Tearing down Playwright...");
        
        if (browser != null) {
            browser.close();
        }
        
        if (playwright != null) {
            playwright.close();
        }
        
        logger.info("Playwright teardown completed");
    }
    
    /**
     * Initialize all page objects with the current page instance
     */
    private void initializePageObjects() {
        homePage = new HomePage(page);
        loginPage = new LoginPage(page);
        productsPage = new ProductsPage(page);
        productDetailsPage = new ProductDetailsPage(page);
        cartPage = new CartPage(page);
        checkoutPage = new CheckoutPage(page);
    }
    
    /**
     * Navigate to the home page
     */
    @Step("Navigate to home page")
    protected HomePage navigateToHome() {
        return homePage.navigate();
    }
    
    /**
     * Navigate to login page
     */
    @Step("Navigate to login page")
    protected LoginPage navigateToLogin() {
        return loginPage.navigate();
    }
    
    /**
     * Navigate to products page
     */
    @Step("Navigate to products page")
    protected ProductsPage navigateToProducts() {
        return productsPage.navigate();
    }
    
    /**
     * Navigate to cart page
     */
    @Step("Navigate to cart page")
    protected CartPage navigateToCart() {
        return cartPage.navigate();
    }
    
    /**
     * Navigate to checkout page
     */
    @Step("Navigate to checkout page")
    protected CheckoutPage navigateToCheckout() {
        return checkoutPage.navigate();
    }
    
    /**
     * Perform login with admin credentials
     */
    @Step("Login as admin")
    protected void loginAsAdmin() {
        navigateToLogin().loginAsAdmin();
        logger.info("Logged in as admin user");
    }
    
    /**
     * Perform login with test user credentials
     */
    @Step("Login as test user")
    protected void loginAsTestUser() {
        navigateToLogin().loginAsTestUser();
        logger.info("Logged in as test user");
    }
    
    /**
     * Take a screenshot with the given name
     */
    @Step("Take screenshot: {name}")
    protected void takeScreenshot(String name) {
        try {
            String fileName = "screenshots/" + name + "_" + System.currentTimeMillis() + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(fileName)));
            logger.info("Screenshot saved: {}", fileName);
        } catch (Exception e) {
            logger.warn("Failed to take screenshot: {}", e.getMessage());
        }
    }
    
    /**
     * Wait for a specified amount of time (use sparingly)
     */
    protected void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted: {}", e.getMessage());
        }
    }
    
    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return page.url();
    }
    
    /**
     * Get page title
     */
    protected String getPageTitle() {
        return page.title();
    }
    
    /**
     * Check if user is on home page
     */
    protected boolean isOnHomePage() {
        return getCurrentUrl().equals(BASE_URL + "/") || getCurrentUrl().equals(BASE_URL);
    }
    
    /**
     * Check if user is on login page
     */
    protected boolean isOnLoginPage() {
        return getCurrentUrl().contains("/login");
    }
    
    /**
     * Refresh the current page
     */
    @Step("Refresh page")
    protected void refreshPage() {
        page.reload();
        logger.info("Page refreshed");
    }
}
