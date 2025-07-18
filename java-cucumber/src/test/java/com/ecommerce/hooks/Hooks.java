package com.ecommerce.hooks;

import com.ecommerce.utils.WebDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    private final WebDriverManager driverManager;

    public Hooks(WebDriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @Before
    public void setUp(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
        driverManager.initializeDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("Finishing scenario: {} - Status: {}", scenario.getName(), scenario.getStatus());

        try {
            // Save trace if scenario failed
            if (scenario.isFailed()) {
                driverManager.saveTrace(scenario.getName(), true);

                // Take screenshot on failure
                byte[] screenshot = driverManager.getPage().screenshot();
                scenario.attach(screenshot, "image/png", "Screenshot");
            }
        } catch (Exception e) {
            logger.warn("Error during scenario teardown: {}", e.getMessage());
        } finally {
            driverManager.quitDriver();
        }
    }
}
