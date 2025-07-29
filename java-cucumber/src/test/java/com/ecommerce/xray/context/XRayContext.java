package com.ecommerce.xray.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * XRay context for capturing test execution metadata.
 * This can be used in step definitions to capture detailed execution information
 * for enhanced XRay reporting.
 */
public class XRayContext {
    private static final Logger logger = LoggerFactory.getLogger(XRayContext.class);
    
    private final List<StepExecution> stepExecutions;
    private String currentScenarioName;
    private String currentTestKey;
    private LocalDateTime scenarioStartTime;
    
    public XRayContext() {
        this.stepExecutions = new ArrayList<>();
    }
    
    /**
     * Start tracking a new scenario
     */
    public void startScenario(String scenarioName) {
        this.currentScenarioName = scenarioName;
        this.scenarioStartTime = LocalDateTime.now();
        this.stepExecutions.clear();
        
        logger.debug("Started XRay tracking for scenario: {}", scenarioName);
    }
    
    /**
     * Start tracking a step execution
     */
    public void startStep(String stepDescription) {
        StepExecution step = new StepExecution();
        step.description = stepDescription;
        step.startTime = LocalDateTime.now();
        step.status = "EXECUTING";
        
        stepExecutions.add(step);
        logger.debug("Started step: {}", stepDescription);
    }
    
    /**
     * End the current step with a status
     */
    public void endStep(String status) {
        if (!stepExecutions.isEmpty()) {
            StepExecution lastStep = stepExecutions.get(stepExecutions.size() - 1);
            lastStep.endTime = LocalDateTime.now();
            lastStep.status = status;
            lastStep.duration = java.time.Duration.between(lastStep.startTime, lastStep.endTime);
            
            logger.debug("Ended step: {} with status: {}", lastStep.description, status);
        }
    }
    
    /**
     * End the current step with status and additional information
     */
    public void endStep(String status, String additionalInfo) {
        endStep(status);
        if (!stepExecutions.isEmpty()) {
            StepExecution lastStep = stepExecutions.get(stepExecutions.size() - 1);
            lastStep.additionalInfo = additionalInfo;
        }
    }
    
    /**
     * Mark the current step as failed with error details
     */
    public void failStep(String errorMessage) {
        endStep("FAIL", errorMessage);
    }
    
    /**
     * Get execution summary for XRay reporting
     */
    public ExecutionSummary getExecutionSummary() {
        ExecutionSummary summary = new ExecutionSummary();
        summary.scenarioName = currentScenarioName;
        summary.testKey = currentTestKey;
        summary.startTime = scenarioStartTime;
        summary.endTime = LocalDateTime.now();
        summary.totalDuration = java.time.Duration.between(scenarioStartTime, summary.endTime);
        summary.stepExecutions = new ArrayList<>(stepExecutions);
        
        // Calculate summary statistics
        summary.totalSteps = stepExecutions.size();
        summary.passedSteps = (int) stepExecutions.stream().filter(s -> "PASS".equals(s.status)).count();
        summary.failedSteps = (int) stepExecutions.stream().filter(s -> "FAIL".equals(s.status)).count();
        summary.overallStatus = summary.failedSteps > 0 ? "FAIL" : "PASS";
        
        return summary;
    }
    
    /**
     * Set the XRay test key for this scenario
     */
    public void setTestKey(String testKey) {
        this.currentTestKey = testKey;
    }
    
    /**
     * Reset context for new scenario
     */
    public void reset() {
        stepExecutions.clear();
        currentScenarioName = null;
        currentTestKey = null;
        scenarioStartTime = null;
    }
    
    /**
     * Inner class representing a step execution
     */
    public static class StepExecution {
        public String description;
        public LocalDateTime startTime;
        public LocalDateTime endTime;
        public java.time.Duration duration;
        public String status;
        public String additionalInfo;
        
        @Override
        public String toString() {
            return String.format("Step: %s [%s] Duration: %s", 
                description, status, duration != null ? duration.toMillis() + "ms" : "N/A");
        }
    }
    
    /**
     * Inner class representing execution summary
     */
    public static class ExecutionSummary {
        public String scenarioName;
        public String testKey;
        public LocalDateTime startTime;
        public LocalDateTime endTime;
        public java.time.Duration totalDuration;
        public List<StepExecution> stepExecutions;
        public int totalSteps;
        public int passedSteps;
        public int failedSteps;
        public String overallStatus;
        
        @Override
        public String toString() {
            return String.format("Scenario: %s [%s] - Steps: %d/%d passed - Duration: %s", 
                scenarioName, overallStatus, passedSteps, totalSteps, 
                totalDuration != null ? totalDuration.toMillis() + "ms" : "N/A");
        }
        
        /**
         * Get formatted execution report
         */
        public String getFormattedReport() {
            StringBuilder report = new StringBuilder();
            report.append("=== XRay Execution Summary ===\\n");
            report.append("Scenario: ").append(scenarioName).append("\\n");
            report.append("Test Key: ").append(testKey != null ? testKey : "N/A").append("\\n");
            report.append("Status: ").append(overallStatus).append("\\n");
            report.append("Duration: ").append(totalDuration != null ? totalDuration.toMillis() + "ms" : "N/A").append("\\n");
            report.append("Steps: ").append(passedSteps).append("/").append(totalSteps).append(" passed\\n");
            
            if (!stepExecutions.isEmpty()) {
                report.append("\\nStep Details:\\n");
                for (StepExecution step : stepExecutions) {
                    report.append("  - ").append(step.toString()).append("\\n");
                    if (step.additionalInfo != null) {
                        report.append("    Info: ").append(step.additionalInfo).append("\\n");
                    }
                }
            }
            
            return report.toString();
        }
    }
}
