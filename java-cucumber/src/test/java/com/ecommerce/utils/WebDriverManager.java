package com.ecommerce.utils;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverManager {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverManager.class);

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private static final String BASE_URL = "http://localhost:3000";
    private static final String BROWSER_TYPE = System.getProperty("browser", "chromium");

    public void initializeDriver() {
        logger.info("Initializing Playwright driver with browser: {}", BROWSER_TYPE);

        playwright = Playwright.create();

        BrowserType browserType = switch (BROWSER_TYPE.toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit", "safari" -> playwright.webkit();
            default -> playwright.chromium();
        };

        browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(100));

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setLocale("en-US")
                .setTimezoneId("America/New_York"));

        // Enable tracing for debugging
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();

        logger.info("Playwright driver initialized successfully");
    }

    public void quitDriver() {
        logger.info("Closing Playwright driver");

        try {
            if (context != null) {
                // Stop tracing
                context.tracing().stop();
                context.close();
            }
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
        } catch (Exception e) {
            logger.warn("Error during driver cleanup: {}", e.getMessage());
        }
    }

    public void saveTrace(String testName, boolean failed) {
        if (context != null && failed) {
            try {
                String tracePath = "test-results/" + testName + "_trace.zip";
                context.tracing().stop(new Tracing.StopOptions().setPath(java.nio.file.Paths.get(tracePath)));
                logger.info("Trace saved to: {}", tracePath);

                // Restart tracing for next test
                context.tracing().start(new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true));
            } catch (Exception e) {
                logger.warn("Error saving trace: {}", e.getMessage());
            }
        }
    }

    public Page getPage() {
        return page;
    }

    public BrowserContext getContext() {
        return context;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }
}
