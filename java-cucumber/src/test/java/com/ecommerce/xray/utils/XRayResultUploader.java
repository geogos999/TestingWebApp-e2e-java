package com.ecommerce.xray.utils;

import com.ecommerce.xray.client.XRayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Utility class to upload test execution results to XRay.
 * This processes Cucumber JSON reports and uploads them to the XSP project.
 */
public class XRayResultUploader {
    private static final Logger logger = LoggerFactory.getLogger(XRayResultUploader.class);
    
    private final XRayClient xrayClient;
    
    public XRayResultUploader() {
        this.xrayClient = new XRayClient();
    }
    
    /**
     * Main method to upload test results
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            logger.error("Usage: XRayResultUploader <cucumber-json-report-path>");
            System.exit(1);
        }
        
        String reportPath = args[0];
        XRayResultUploader uploader = new XRayResultUploader();
        uploader.uploadResults(reportPath);
    }
    
    /**
     * Upload test results to XRay
     */
    public void uploadResults(String cucumberJsonPath) {
        if (!xrayClient.authenticate()) {
            logger.error("Failed to authenticate with XRay. Check your credentials.");
            return;
        }
        
        // Validate the report file exists
        File reportFile = new File(cucumberJsonPath);
        if (!reportFile.exists()) {
            logger.error("Cucumber JSON report not found: {}", cucumberJsonPath);
            return;
        }
        
        logger.info("Uploading test results from: {}", cucumberJsonPath);
        
        // Upload the results
        boolean success = xrayClient.uploadTestResults(cucumberJsonPath);
        
        if (success) {
            logger.info("✅ Successfully uploaded test results to XRay (XSP project)");
            
            // Optionally create a test execution ticket
            if (xrayClient.getConfig().autoCreateTestExecution()) {
                createTestExecution();
            }
        } else {
            logger.error("❌ Failed to upload test results to XRay");
        }
    }
    
    /**
     * Create a test execution ticket in XRay
     */
    private void createTestExecution() {
        try {
            String summary = "Automated Test Execution - " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            // Note: In a real implementation, you would extract test keys from the JSON report
            // For now, we'll create a basic test execution
            String testExecutionKey = xrayClient.createTestExecution(summary, Arrays.asList());
            
            if (testExecutionKey != null) {
                logger.info("✅ Created test execution: {}", testExecutionKey);
            } else {
                logger.warn("❌ Failed to create test execution");
            }
            
        } catch (Exception e) {
            logger.error("Error creating test execution", e);
        }
    }
    
    /**
     * Upload results with custom test execution summary
     */
    public void uploadResults(String cucumberJsonPath, String executionSummary) {
        uploadResults(cucumberJsonPath);
        
        if (executionSummary != null && !executionSummary.trim().isEmpty()) {
            String testExecutionKey = xrayClient.createTestExecution(executionSummary, Arrays.asList());
            if (testExecutionKey != null) {
                logger.info("✅ Created custom test execution: {} - {}", testExecutionKey, executionSummary);
            }
        }
    }
}
