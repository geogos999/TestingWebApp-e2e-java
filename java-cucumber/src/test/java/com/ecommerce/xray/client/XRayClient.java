package com.ecommerce.xray.client;

import com.ecommerce.xray.config.XRayConfig;
import com.ecommerce.xray.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * XRay REST API Client for Dirty Boots Studios (XSP project).
 * Handles authentication, test creation, and result uploads to XRay/Jira.
 * Uses Java 11 HttpClient for HTTP operations.
 */
public class XRayClient {
    private static final Logger logger = LoggerFactory.getLogger(XRayClient.class);
    
    private final XRayConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String authToken;

    public XRayClient() {
        this.config = ConfigFactory.create(XRayConfig.class);
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
        this.objectMapper = new ObjectMapper();
        
        logger.info("XRay Client initialized for project: {}", config.projectKey());
    }

    /**
     * Authenticate with XRay Cloud API
     */
    public boolean authenticate() {
        if (config.clientId() == null || config.clientSecret() == null) {
            logger.warn("XRay credentials not configured. Set XRAY_CLIENT_ID and XRAY_CLIENT_SECRET environment variables.");
            return false;
        }

        try {
            String authUrl = "https://xray.cloud.getxray.app/api/v1/authenticate";
            
            String authPayload = String.format("{\"client_id\":\"%s\",\"client_secret\":\"%s\"}", 
                config.clientId(), config.clientSecret());
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(authUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(authPayload))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                this.authToken = response.body().replace("\"", "");
                logger.info("Successfully authenticated with XRay");
                return true;
            } else {
                logger.error("XRay authentication failed: {}", response.statusCode());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error during XRay authentication", e);
            return false;
        }
    }

    /**
     * Create a test issue in XRay from a Cucumber scenario
     */
    public String createTestIssue(TestIssueRequest testRequest) {
        if (!isAuthenticated()) {
            logger.warn("Not authenticated with XRay. Cannot create test issue.");
            return null;
        }

        try {
            String createUrl = "https://xray.cloud.getxray.app/api/v1/import/feature";
            
            // Create the test payload
            String payload = objectMapper.writeValueAsString(testRequest);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(createUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                logger.info("Successfully created test issue: {}", responseBody);
                return extractTestKey(responseBody);
            } else {
                logger.error("Failed to create test issue: {}", response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error creating test issue", e);
            return null;
        }
    }

    /**
     * Upload test execution results to XRay
     */
    public boolean uploadTestResults(String cucumberJsonPath) {
        if (!isAuthenticated()) {
            logger.warn("Not authenticated with XRay. Cannot upload results.");
            return false;
        }

        try {
            String uploadUrl = "https://xray.cloud.getxray.app/api/v1/import/execution/cucumber";
            
            // Read the Cucumber JSON report
            java.io.File jsonFile = new java.io.File(cucumberJsonPath);
            if (!jsonFile.exists()) {
                logger.error("Cucumber JSON report not found: {}", cucumberJsonPath);
                return false;
            }

            String jsonContent = java.nio.file.Files.readString(jsonFile.toPath());
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonContent))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                logger.info("Successfully uploaded test results to XRay");
                return true;
            } else {
                logger.error("Failed to upload test results: {}", response.statusCode());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error uploading test results", e);
            return false;
        }
    }

    /**
     * Create a test execution ticket in XRay
     */
    public String createTestExecution(String summary, List<String> testKeys) {
        if (!isAuthenticated()) {
            logger.warn("Not authenticated with XRay. Cannot create test execution.");
            return null;
        }

        try {
            String createUrl = String.format("%s/rest/api/2/issue", config.jiraUrl());
            
            TestExecutionRequest executionRequest = TestExecutionRequest.builder()
                .projectKey(config.projectKey())
                .summary(summary != null ? summary : "Automated Test Execution - " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .description("Automated test execution for E-Commerce application")
                .testEnvironment(config.testEnvironment())
                .version(config.defaultVersion())
                .tests(testKeys)
                .build();
            
            String payload = objectMapper.writeValueAsString(executionRequest);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(createUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 201) {
                String responseBody = response.body();
                logger.info("Successfully created test execution: {}", responseBody);
                return extractTestExecutionKey(responseBody);
            } else {
                logger.error("Failed to create test execution: {}", response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error creating test execution", e);
            return null;
        }
    }

    private boolean isAuthenticated() {
        return authToken != null && !authToken.isEmpty();
    }

    private String extractTestKey(String responseBody) {
        try {
            // Parse response to extract test key
            // This is a simplified extraction - actual implementation would parse JSON
            return "XSP-" + System.currentTimeMillis() % 10000; // Placeholder
        } catch (Exception e) {
            logger.warn("Could not extract test key from response", e);
            return null;
        }
    }

    private String extractTestExecutionKey(String responseBody) {
        try {
            // Parse response to extract test execution key
            // This is a simplified extraction - actual implementation would parse JSON
            return "XSP-" + (System.currentTimeMillis() % 10000 + 1000); // Placeholder
        } catch (Exception e) {
            logger.warn("Could not extract test execution key from response", e);
            return null;
        }
    }

    public XRayConfig getConfig() {
        return config;
    }
}
