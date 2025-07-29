package com.ecommerce.xray.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request model for creating test issues in XRay.
 * Used for Dirty Boots Studios XSP project.
 */
public class TestIssueRequest {
    
    @JsonProperty("fields")
    private TestIssueFields fields;
    
    public TestIssueRequest() {}
    
    public TestIssueRequest(TestIssueFields fields) {
        this.fields = fields;
    }
    
    public TestIssueFields getFields() {
        return fields;
    }
    
    public void setFields(TestIssueFields fields) {
        this.fields = fields;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String projectKey = "XSP";
        private String testType = "Cucumber";
        private String summary;
        private String description;
        private String gherkinDefinition;
        private List<String> labels;
        
        public Builder projectKey(String projectKey) {
            this.projectKey = projectKey;
            return this;
        }
        
        public Builder testType(String testType) {
            this.testType = testType;
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
        
        public Builder gherkinDefinition(String gherkinDefinition) {
            this.gherkinDefinition = gherkinDefinition;
            return this;
        }
        
        public Builder labels(List<String> labels) {
            this.labels = labels;
            return this;
        }
        
        public TestIssueRequest build() {
            TestIssueFields fields = new TestIssueFields();
            fields.setProject(new Project(projectKey));
            fields.setSummary(summary);
            fields.setDescription(description);
            fields.setIssueType(new IssueType("Test"));
            fields.setTestType(new TestType(testType));
            fields.setGherkinDefinition(gherkinDefinition);
            fields.setLabels(labels);
            
            return new TestIssueRequest(fields);
        }
    }
}

class TestIssueFields {
    @JsonProperty("project")
    private Project project;
    
    @JsonProperty("summary")
    private String summary;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("issuetype")
    private IssueType issueType;
    
    @JsonProperty("customfield_10100") // Test Type field in XRay
    private TestType testType;
    
    @JsonProperty("customfield_10200") // Gherkin Definition field in XRay
    private String gherkinDefinition;
    
    @JsonProperty("labels")
    private List<String> labels;
    
    // Getters and setters
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public IssueType getIssueType() { return issueType; }
    public void setIssueType(IssueType issueType) { this.issueType = issueType; }
    
    public TestType getTestType() { return testType; }
    public void setTestType(TestType testType) { this.testType = testType; }
    
    public String getGherkinDefinition() { return gherkinDefinition; }
    public void setGherkinDefinition(String gherkinDefinition) { this.gherkinDefinition = gherkinDefinition; }
    
    public List<String> getLabels() { return labels; }
    public void setLabels(List<String> labels) { this.labels = labels; }
}

class Project {
    @JsonProperty("key")
    private String key;
    
    public Project() {}
    
    public Project(String key) {
        this.key = key;
    }
    
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}

class IssueType {
    @JsonProperty("name")
    private String name;
    
    public IssueType() {}
    
    public IssueType(String name) {
        this.name = name;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

class TestType {
    @JsonProperty("value")
    private String value;
    
    public TestType() {}
    
    public TestType(String value) {
        this.value = value;
    }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
