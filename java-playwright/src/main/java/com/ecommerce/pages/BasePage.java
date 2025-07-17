package com.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

/**
 * Base page class containing common methods and utilities for all page objects.
 * This class implements the Page Object Model pattern and provides reusable methods
 * for interacting with web elements.
 */
public abstract class BasePage {
    protected final Page page;
    protected final Logger logger;
    
    // Common timeouts
    protected static final int DEFAULT_TIMEOUT = 10000;
    protected static final int LONG_TIMEOUT = 30000;
    
    public BasePage(Page page) {
        this.page = page;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    // Navigation methods
    @Step("Navigate to URL: {url}")
    public void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        page.navigate(url);
        waitForPageLoad();
    }
    
    @Step("Wait for page to load")
    public void waitForPageLoad() {
        page.waitForLoadState();
        logger.info("Page loaded successfully");
    }
    
    // Element interaction methods
    @Step("Click element: {selector}")
    public void clickElement(String selector) {
        logger.info("Clicking element: {}", selector);
        page.locator(selector).click();
    }
    
    @Step("Fill input field: {selector} with text: {text}")
    public void fillInput(String selector, String text) {
        logger.info("Filling input {} with text: {}", selector, text);
        page.locator(selector).fill(text);
    }
    
    @Step("Clear input field: {selector}")
    public void clearInput(String selector) {
        logger.info("Clearing input: {}", selector);
        page.locator(selector).clear();
    }
    
    @Step("Get text from element: {selector}")
    public String getText(String selector) {
        String text = page.locator(selector).textContent();
        logger.info("Retrieved text '{}' from element: {}", text, selector);
        return text != null ? text.trim() : "";
    }
    
    @Step("Get attribute {attribute} from element: {selector}")
    public String getAttribute(String selector, String attribute) {
        String value = page.locator(selector).getAttribute(attribute);
        logger.info("Retrieved attribute '{}' = '{}' from element: {}", attribute, value, selector);
        return value;
    }
    
    // Visibility and state methods
    @Step("Check if element is visible: {selector}")
    public boolean isElementVisible(String selector) {
        boolean isVisible = page.locator(selector).isVisible();
        logger.info("Element {} visibility: {}", selector, isVisible);
        return isVisible;
    }
    
    @Step("Check if element is enabled: {selector}")
    public boolean isElementEnabled(String selector) {
        boolean isEnabled = page.locator(selector).isEnabled();
        logger.info("Element {} enabled state: {}", selector, isEnabled);
        return isEnabled;
    }
    
    @Step("Check if element is checked: {selector}")
    public boolean isElementChecked(String selector) {
        boolean isChecked = page.locator(selector).isChecked();
        logger.info("Element {} checked state: {}", selector, isChecked);
        return isChecked;
    }
    
    // Wait methods
    @Step("Wait for element to be visible: {selector}")
    public Locator waitForElementVisible(String selector) {
        return waitForElementVisible(selector, DEFAULT_TIMEOUT);
    }
    
    @Step("Wait for element to be visible: {selector} with timeout: {timeout}ms")
    public Locator waitForElementVisible(String selector, int timeout) {
        logger.info("Waiting for element to be visible: {} (timeout: {}ms)", selector, timeout);
        return page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeout));
    }
    
    @Step("Wait for element to be hidden: {selector}")
    public void waitForElementHidden(String selector) {
        waitForElementHidden(selector, DEFAULT_TIMEOUT);
    }
    
    @Step("Wait for element to be hidden: {selector} with timeout: {timeout}ms")
    public void waitForElementHidden(String selector, int timeout) {
        logger.info("Waiting for element to be hidden: {} (timeout: {}ms)", selector, timeout);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeout));
    }
    
    @Step("Wait for URL to match: {urlPattern}")
    public void waitForUrl(String urlPattern) {
        logger.info("Waiting for URL to match: {}", urlPattern);
        page.waitForURL(urlPattern);
    }
    
    // Utility methods
    @Step("Get current URL")
    public String getCurrentUrl() {
        String url = page.url();
        logger.info("Current URL: {}", url);
        return url;
    }
    
    @Step("Get page title")
    public String getPageTitle() {
        String title = page.title();
        logger.info("Page title: {}", title);
        return title;
    }
    
    @Step("Refresh page")
    public void refreshPage() {
        logger.info("Refreshing page");
        page.reload();
        waitForPageLoad();
    }
    
    @Step("Scroll element into view: {selector}")
    public void scrollToElement(String selector) {
        logger.info("Scrolling to element: {}", selector);
        page.locator(selector).scrollIntoViewIfNeeded();
    }
    
    @Step("Hover over element: {selector}")
    public void hoverOverElement(String selector) {
        logger.info("Hovering over element: {}", selector);
        page.locator(selector).hover();
    }
    
    // Screenshot methods
    @Step("Take screenshot: {fileName}")
    public void takeScreenshot(String fileName) {
        String filePath = "screenshots/" + fileName + ".png";
        logger.info("Taking screenshot: {}", filePath);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
    }
    
    @Step("Take full page screenshot: {fileName}")
    public void takeFullPageScreenshot(String fileName) {
        String filePath = "screenshots/" + fileName + "_fullpage.png";
        logger.info("Taking full page screenshot: {}", filePath);
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(filePath))
                .setFullPage(true));
    }
    
    // Dropdown and select methods
    @Step("Select option by text: {optionText} from dropdown: {selector}")
    public void selectOptionByText(String selector, String optionText) {
        logger.info("Selecting option '{}' from dropdown: {}", optionText, selector);
        page.locator(selector).selectOption(optionText);
    }
    
    @Step("Select option by value: {optionValue} from dropdown: {selector}")
    public void selectOptionByValue(String selector, String optionValue) {
        logger.info("Selecting option value '{}' from dropdown: {}", optionValue, selector);
        page.locator(selector).selectOption(new String[]{optionValue});
    }
    
    // Alert and dialog handling
    @Step("Accept browser dialog")
    public void acceptDialog() {
        logger.info("Setting up dialog handler to accept");
        page.onDialog(dialog -> {
            logger.info("Accepting dialog: {}", dialog.message());
            dialog.accept();
        });
    }
    
    @Step("Dismiss browser dialog")
    public void dismissDialog() {
        logger.info("Setting up dialog handler to dismiss");
        page.onDialog(dialog -> {
            logger.info("Dismissing dialog: {}", dialog.message());
            dialog.dismiss();
        });
    }
    
    // Count methods
    @Step("Count elements matching selector: {selector}")
    public int getElementCount(String selector) {
        int count = page.locator(selector).count();
        logger.info("Found {} elements matching selector: {}", count, selector);
        return count;
    }
    
    // Validation helpers
    @Step("Verify element contains text: {expectedText}")
    public boolean elementContainsText(String selector, String expectedText) {
        String actualText = getText(selector);
        boolean contains = actualText.contains(expectedText);
        logger.info("Element {} contains text '{}': {}", selector, expectedText, contains);
        return contains;
    }
    
    @Step("Verify page title contains: {expectedTitle}")
    public boolean pageTitleContains(String expectedTitle) {
        String actualTitle = getPageTitle();
        boolean contains = actualTitle.contains(expectedTitle);
        logger.info("Page title contains '{}': {}", expectedTitle, contains);
        return contains;
    }
}
