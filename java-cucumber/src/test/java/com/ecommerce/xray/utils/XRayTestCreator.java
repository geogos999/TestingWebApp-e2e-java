package com.ecommerce.xray.utils;

import com.ecommerce.xray.client.XRayClient;
import com.ecommerce.xray.model.TestIssueRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Utility class to create XRay test issues from Cucumber feature files.
 * This will parse your existing login.feature and products.feature files
 * and create corresponding test issues in the XSP project.
 */
public class XRayTestCreator {
    private static final Logger logger = LoggerFactory.getLogger(XRayTestCreator.class);
    
    private final XRayClient xrayClient;
    
    // Regex patterns for parsing Gherkin
    private static final Pattern FEATURE_PATTERN = Pattern.compile("^Feature:\\s*(.+)$", Pattern.MULTILINE);
    private static final Pattern SCENARIO_PATTERN = Pattern.compile("^\\s*(?:@\\w+\\s*)*Scenario(?:\\s+Outline)?:\\s*(.+)$", Pattern.MULTILINE);
    private static final Pattern TAGS_PATTERN = Pattern.compile("@(\\w+)", Pattern.MULTILINE);
    
    public XRayTestCreator() {
        this.xrayClient = new XRayClient();
    }
    
    /**
     * Main method to create tests from all feature files
     */
    public static void main(String[] args) {
        String featuresPath = args.length > 0 ? args[0] : "src/test/resources/features/";
        
        XRayTestCreator creator = new XRayTestCreator();
        creator.createTestsFromFeatureFiles(featuresPath);
    }
    
    /**
     * Process all feature files in the specified directory
     */
    public void createTestsFromFeatureFiles(String featuresDirectory) {
        if (!xrayClient.authenticate()) {
            logger.error("Failed to authenticate with XRay. Check your credentials.");
            return;
        }
        
        try {
            Path featuresPath = Paths.get(featuresDirectory);
            if (!Files.exists(featuresPath)) {
                logger.error("Features directory does not exist: {}", featuresDirectory);
                return;
            }
            
            // Process all .feature files
            try (Stream<Path> paths = Files.walk(featuresPath)) {
                paths.filter(Files::isRegularFile)
                     .filter(path -> path.toString().endsWith(".feature"))
                     .forEach(this::processFeatureFile);
            }
            
        } catch (IOException e) {
            logger.error("Error processing feature files", e);
        }
    }
    
    /**
     * Process a single feature file
     */
    private void processFeatureFile(Path featureFile) {
        try {
            String content = Files.readString(featureFile);
            logger.info("Processing feature file: {}", featureFile.getFileName());
            
            // Extract feature info
            String featureName = extractFeatureName(content);
            // Note: Feature tags are available but not currently used in test creation
            // List<String> featureTags = extractTags(content, 0);
            
            // Extract and create tests for each scenario
            List<ScenarioInfo> scenarios = extractScenarios(content);
            
            for (ScenarioInfo scenario : scenarios) {
                createTestFromScenario(featureName, scenario, featureFile.getFileName().toString());
            }
            
        } catch (IOException e) {
            logger.error("Error reading feature file: {}", featureFile, e);
        }
    }
    
    /**
     * Create an XRay test issue from a scenario
     */
    private void createTestFromScenario(String featureName, ScenarioInfo scenario, String fileName) {
        try {
            // Build test issue request
            TestIssueRequest testRequest = TestIssueRequest.builder()
                .projectKey("XSP")
                .testType("Cucumber")
                .summary(scenario.name)
                .description(String.format("Automated test from %s feature\\n\\nFeature: %s", fileName, featureName))
                .gherkinDefinition(scenario.gherkinText)
                .labels(Arrays.asList("automation", "e2e", "cucumber"))
                .build();
            
            // Create the test issue
            String testKey = xrayClient.createTestIssue(testRequest);
            
            if (testKey != null) {
                logger.info("✅ Created test {} for scenario: {}", testKey, scenario.name);
            } else {
                logger.warn("❌ Failed to create test for scenario: {}", scenario.name);
            }
            
        } catch (Exception e) {
            logger.error("Error creating test for scenario: {}", scenario.name, e);
        }
    }
    
    /**
     * Extract feature name from content
     */
    private String extractFeatureName(String content) {
        Matcher matcher = FEATURE_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1).trim() : "Unknown Feature";
    }
    
    /**
     * Extract scenarios from feature content
     */
    private List<ScenarioInfo> extractScenarios(String content) {
        List<ScenarioInfo> scenarios = new java.util.ArrayList<>();
        String[] lines = content.split("\\n");
        
        ScenarioInfo currentScenario = null;
        StringBuilder gherkinBuilder = new StringBuilder();
        boolean inScenario = false;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            // Check if this line starts a new scenario
            Matcher scenarioMatcher = SCENARIO_PATTERN.matcher(line);
            if (scenarioMatcher.find()) {
                // Save previous scenario if exists
                if (currentScenario != null) {
                    currentScenario.gherkinText = gherkinBuilder.toString().trim();
                    scenarios.add(currentScenario);
                }
                
                // Start new scenario
                currentScenario = new ScenarioInfo();
                currentScenario.name = scenarioMatcher.group(1).trim();
                currentScenario.tags = extractTagsFromPreviousLines(lines, i);
                
                gherkinBuilder = new StringBuilder();
                gherkinBuilder.append(line).append("\\n");
                inScenario = true;
                
            } else if (inScenario) {
                // Check if we've reached the end of the scenario
                if (line.trim().isEmpty() && i + 1 < lines.length && 
                    (lines[i + 1].trim().startsWith("@") || 
                     lines[i + 1].trim().startsWith("Scenario") ||
                     lines[i + 1].trim().startsWith("Feature"))) {
                    inScenario = false;
                } else if (!line.trim().isEmpty()) {
                    gherkinBuilder.append(line).append("\\n");
                }
            }
        }
        
        // Don't forget the last scenario
        if (currentScenario != null) {
            currentScenario.gherkinText = gherkinBuilder.toString().trim();
            scenarios.add(currentScenario);
        }
        
        return scenarios;
    }
    
    /**
     * Extract tags from content starting at a specific position
     * @param content The content to search
     * @param startPos Starting position
     * @return List of extracted tags
     * @deprecated Currently unused but kept for potential future feature enhancements
     */
    @Deprecated
    @SuppressWarnings("unused")
    private List<String> extractTags(String content, int startPos) {
        List<String> tags = new java.util.ArrayList<>();
        Matcher matcher = TAGS_PATTERN.matcher(content);
        while (matcher.find(startPos)) {
            tags.add(matcher.group(1));
        }
        return tags;
    }
    
    /**
     * Extract tags from the lines preceding a scenario
     */
    private List<String> extractTagsFromPreviousLines(String[] lines, int scenarioLineIndex) {
        List<String> tags = new java.util.ArrayList<>();
        
        // Look backwards for tag lines
        for (int i = scenarioLineIndex - 1; i >= 0; i--) {
            String line = lines[i].trim();
            if (line.startsWith("@")) {
                // Extract all tags from this line
                Matcher matcher = TAGS_PATTERN.matcher(line);
                while (matcher.find()) {
                    tags.add(0, matcher.group(1)); // Add at beginning to maintain order
                }
            } else if (!line.isEmpty()) {
                // Non-empty, non-tag line - stop looking
                break;
            }
        }
        
        return tags;
    }
    
    /**
     * Inner class to hold scenario information
     */
    private static class ScenarioInfo {
        String name;
        List<String> tags;
        String gherkinText;
        
        @Override
        public String toString() {
            return String.format("Scenario: %s (tags: %s)", name, tags);
        }
    }
}
