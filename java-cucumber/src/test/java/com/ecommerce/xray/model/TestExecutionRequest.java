package com.ecommerce.xray.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request model for creating test execution tickets in XRay.
 * Used for Dirty Boots Studios XSP project.
 */
public class TestExecutionRequest {
    
    @JsonProperty("fields")
    private TestExecutionFields fields;
    
    public TestExecutionRequest() {}
    
    public TestExecutionRequest(TestExecutionFields fields) {
        this.fields = fields;
    }
    
    public TestExecutionFields getFields() {
        return fields;
    }
    
    public void setFields(TestExecutionFields fields) {
        this.fields = fields;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String projectKey = "XSP";
        private String summary;
        private String description;
        private String testEnvironment;
        private String version;
        private List<String> tests;
        
        public Builder projectKey(String projectKey) {
            this.projectKey = projectKey;
            return this;
        }
        
        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder testEnvironment(String testEnvironment) {
            this.testEnvironment = testEnvironment;
            return this;
        }
        
        public Builder version(String version) {
            this.version = version;
            return this;
        }
        
        public Builder tests(List<String> tests) {
            this.tests = tests;
            return this;
        }
        
        public TestExecutionRequest build() {
            TestExecutionFields fields = new TestExecutionFields();
            fields.setProject(new Project(projectKey));
            fields.setSummary(summary);
            fields.setDescription(description);
            fields.setIssueType(new IssueType("Test Execution"));
            fields.setTestEnvironment(testEnvironment);
            fields.setVersion(version);
            fields.setTests(tests);
            
            return new TestExecutionRequest(fields);
        }
    }
}

class TestExecutionFields {
    @JsonProperty("project")
    private Project project;
    
    @JsonProperty("summary")
    private String summary;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("issuetype")
    private IssueType issueType;
    
    @JsonProperty("customfield_10300") // Test Environment field in XRay
    private String testEnvironment;
    
    @JsonProperty("customfield_10400") // Version field in XRay
    private String version;
    
    @JsonProperty("customfield_10500") // Tests field in XRay
    private List<String> tests;
    
    // Getters and setters
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public IssueType getIssueType() { return issueType; }
    public void setIssueType(IssueType issueType) { this.issueType = issueType; }
    
    public String getTestEnvironment() { return testEnvironment; }
    public void setTestEnvironment(String testEnvironment) { this.testEnvironment = testEnvironment; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public List<String> getTests() { return tests; }
    public void setTests(List<String> tests) { this.tests = tests; }
}
