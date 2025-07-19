package com.ecommerce.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String BASE_URL = "http://localhost:3000";
    private static final String BROWSER_TYPE = System.getProperty("browser", "chrome");
    private static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    public void initializeDriver() {
        logger.info("Initializing Selenium WebDriver with browser: {}, headless: {}", BROWSER_TYPE, HEADLESS);

        switch (BROWSER_TYPE.toLowerCase()) {
            case "firefox":
                io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (HEADLESS) {
                    firefoxOptions.addArguments("--headless");
                }
                firefoxOptions.addArguments("--window-size=1920,1080");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (HEADLESS) {
                    edgeOptions.addArguments("--headless");
                }
                edgeOptions.addArguments("--window-size=1920,1080");
                edgeOptions.addArguments("--disable-web-security");
                edgeOptions.addArguments("--allow-running-insecure-content");
                driver = new EdgeDriver(edgeOptions);
                break;
            default:
                io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (HEADLESS) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-web-security");
                chromeOptions.addArguments("--allow-running-insecure-content");
                chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);

        logger.info("Selenium WebDriver initialized successfully");
    }

    public void quitDriver() {
        logger.info("Closing Selenium WebDriver");

        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            logger.warn("Error during driver cleanup: {}", e.getMessage());
        }
    }

    public byte[] takeScreenshot() {
        try {
            if (driver != null) {
                return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception e) {
            logger.warn("Error taking screenshot: {}", e.getMessage());
        }
        return new byte[0];
    }

    public void saveScreenshot(String testName) {
        try {
            if (driver != null) {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotPath = "test-results/screenshots/" + testName + "_screenshot.png";

                // Create directory if it doesn't exist
                Files.createDirectories(Paths.get("test-results/screenshots"));

                Files.copy(screenshot.toPath(), Paths.get(screenshotPath));
                logger.info("Screenshot saved to: {}", screenshotPath);
            }
        } catch (IOException e) {
            logger.warn("Error saving screenshot: {}", e.getMessage());
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }
}
