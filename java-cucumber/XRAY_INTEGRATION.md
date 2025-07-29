# XRay Integration for Dirty Boots Studios (XSP Project)

This document explains how to use the XRay integration with your Java/Cucumber testing framework.

## Setup

### 1. Environment Variables
Set up your XRay credentials as environment variables:

```bash
export XRAY_CLIENT_ID="your_xray_client_id"
export XRAY_CLIENT_SECRET="your_xray_client_secret"
```

### 2. Configuration
XRay settings are configured in `src/test/resources/xray.properties`. The key settings for XSP project:

- `xray.project.key=XSP` (Dirty Boots Studios project)
- `xray.jira.url=https://dirtybootsstudios.atlassian.net`
- `xray.test.environment=localhost:3000`

## Usage

### Creating Test Issues from Feature Files

This will parse your existing `login.feature` and `products.feature` files and create corresponding test issues in XRay:

```bash
# Create all test issues from feature files
./gradlew xrayCreateTests
```

**Expected Output:**
```
✅ Created test XSP-123 for scenario: Successful login with valid credentials
✅ Created test XSP-124 for scenario: Failed login with invalid credentials
✅ Created test XSP-125 for scenario: Login attempts with different credential combinations
✅ Created test XSP-126 for scenario: Search for a product successfully
✅ Created test XSP-127 for scenario: Add product to shopping cart
```

### Running Tests and Uploading Results

```bash
# Run tests and upload results to XRay
./gradlew test xrayUploadResults
```

### Complete XRay Workflow

```bash
# Complete workflow: create tests, run tests, upload results
./gradlew xrayFullWorkflow
```

## Available Gradle Tasks

| Task | Description |
|------|-------------|
| `xrayCreateTests` | Create test issues in XRay from Cucumber scenarios |
| `xrayUploadResults` | Upload test execution results to XRay |
| `xrayFullWorkflow` | Complete workflow (create → test → upload) |

## What Gets Created in XRay

### Test Issues
For each Cucumber scenario, a test issue is created with:
- **Project**: XSP (Dirty Boots Studios)
- **Issue Type**: Test
- **Test Type**: Cucumber
- **Summary**: Scenario name (e.g., "Successful login with valid credentials")
- **Description**: Feature name and file reference
- **Gherkin Definition**: Complete scenario text
- **Labels**: automation, e2e, cucumber

### Test Executions
When uploading results, XRay creates:
- **Test Execution** tickets linking multiple test runs
- **Execution results** for each test (PASS/FAIL)
- **Test environment**: localhost:3000
- **Version**: 1.0.0

## Example: Your Current Tests

Based on your `login.feature`, these tests would be created:

1. **XSP-XXX**: "Successful login with valid credentials"
   - Tags: @smoke, @login
   - Status: Tracks execution results

2. **XSP-XXX**: "Failed login with invalid credentials"  
   - Tags: @login, @negative
   - Status: Tracks execution results

3. **XSP-XXX**: "Login attempts with different credential combinations"
   - Tags: @login
   - Status: Tracks execution results with data-driven testing

## CI/CD Integration (Disabled)

CI/CD tasks are commented out in `build.gradle`. To enable:

1. Uncomment the `xrayCICreateTests` and `xrayCIUploadResults` tasks
2. Set environment variables in your CI system:
   ```yaml
   # GitHub Actions example
   env:
     CI: true
     XRAY_ENABLED: true
     XRAY_CLIENT_ID: ${{ secrets.XRAY_CLIENT_ID }}
     XRAY_CLIENT_SECRET: ${{ secrets.XRAY_CLIENT_SECRET }}
   ```

## Troubleshooting

### Authentication Issues
```bash
# Check if credentials are set
echo $XRAY_CLIENT_ID
echo $XRAY_CLIENT_SECRET

# Test authentication manually
./gradlew xrayCreateTests --debug
```

### No Tests Created
- Verify feature files exist in `src/test/resources/features/`
- Check that scenarios are properly formatted Gherkin
- Ensure XRay project (XSP) has proper permissions

### Upload Failures
- Verify Cucumber JSON report exists: `build/reports/cucumber/cucumber.json`
- Run tests first: `./gradlew test`
- Check network connectivity to Jira/XRay

## Benefits

✅ **Automatic Test Issue Creation** - No manual test case creation in Jira
✅ **Bi-directional Sync** - Code changes reflected in test management
✅ **Rich Reporting** - Detailed execution results in XRay dashboards  
✅ **Traceability** - Link tests to requirements and defects
✅ **Team Collaboration** - Business stakeholders can view test status in Jira

## Next Steps

1. Set up XRay credentials
2. Run `./gradlew xrayCreateTests` to create initial test issues
3. Execute `./gradlew test xrayUploadResults` to see results in XRay
4. Configure CI/CD integration when ready
5. Explore XRay dashboards and reporting features in Jira
