package com.ecommerce.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Debug test to inspect the actual HTML structure of the application
 */
public class DebugTest {

    private static final Logger logger = LoggerFactory.getLogger(DebugTest.class);

    @Test
    public void inspectPageStructure() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setSlowMo(1000));

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Navigate to the application
            page.navigate("http://localhost:3000");

            // Wait for the page to load
            page.waitForLoadState();

            // Get the page title
            String title = page.title();
            logger.info("Page title: {}", title);

            // Get the page HTML (first 2000 characters)
            String html = page.content();
            logger.info("Page HTML structure (first 2000 chars): {}", html.substring(0, Math.min(2000, html.length())));

            // Check for common header elements
            checkElement(page, "header");
            checkElement(page, ".header");
            checkElement(page, "#header");
            checkElement(page, "nav");
            checkElement(page, ".nav");
            checkElement(page, ".navbar");
            checkElement(page, "[data-testid='header']");

            // Check for hero section elements
            checkElement(page, ".hero");
            checkElement(page, ".hero-section");
            checkElement(page, "#hero");
            checkElement(page, "[data-testid='hero-section']");

            // Take a screenshot
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("debug-screenshot.png")));
            logger.info("Screenshot saved as debug-screenshot.png");

            // Keep browser open for 5 seconds to manually inspect
            page.waitForTimeout(5000);

            context.close();
            browser.close();
        }
    }

    private void checkElement(Page page, String selector) {
        try {
            if (page.locator(selector).count() > 0) {
                logger.info("✓ Found element: {}", selector);
                // Get the element's text content (first 100 chars)
                String text = page.locator(selector).first().textContent();
                if (text != null && !text.trim().isEmpty()) {
                    logger.info("  Text content: {}", text.substring(0, Math.min(100, text.length())));
                }
            } else {
                logger.info("✗ Element not found: {}", selector);
            }
        } catch (Exception e) {
            logger.info("✗ Error checking element {}: {}", selector, e.getMessage());
        }
    }
}
