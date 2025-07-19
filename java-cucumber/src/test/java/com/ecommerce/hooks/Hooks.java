package com.ecommerce.hooks;

import com.ecommerce.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    private final DriverManager driverManager;

    public Hooks(DriverManager driverManager) {
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
            // Take screenshot and save if scenario failed
            if (scenario.isFailed()) {
                // Save screenshot to file for debugging
                driverManager.saveScreenshot(scenario.getName());

                // Attach screenshot to Cucumber report
                byte[] screenshot = driverManager.takeScreenshot();
                if (screenshot.length > 0) {
                    scenario.attach(screenshot, "image/png", "Screenshot");
                }
            }
        } catch (Exception e) {
            logger.warn("Error during scenario teardown: {}", e.getMessage());
        } finally {
            driverManager.quitDriver();
        }
    }
}
