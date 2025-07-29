package com.ecommerce.xray.config;

import org.aeonbits.owner.Config;

/**
 * XRay configuration interface using Owner library for property management.
 * This manages all XRay/Jira integration settings for the Dirty Boots Studios project.
 */
@Config.Sources({
    "classpath:xray.properties",
    "system:properties",
    "system:env"
})
public interface XRayConfig extends Config {

    @Key("xray.jira.url")
    @DefaultValue("https://dirtybootsstudios.atlassian.net")
    String jiraUrl();

    @Key("xray.project.key")
    @DefaultValue("XSP")
    String projectKey();

    @Key("xray.client.id")
    String clientId();

    @Key("xray.client.secret")
    String clientSecret();

    @Key("xray.test.type")
    @DefaultValue("Cucumber")
    String testType();

    @Key("xray.create.tests.enabled")
    @DefaultValue("true")
    boolean createTestsEnabled();

    @Key("xray.update.existing.tests")
    @DefaultValue("true")
    boolean updateExistingTests();

    @Key("xray.auto.create.test.execution")
    @DefaultValue("true")
    boolean autoCreateTestExecution();

    @Key("xray.upload.results.enabled")
    @DefaultValue("true")
    boolean uploadResultsEnabled();

    @Key("xray.test.environment")
    @DefaultValue("localhost:3000")
    String testEnvironment();

    @Key("xray.default.version")
    @DefaultValue("1.0.0")
    String defaultVersion();

    @Key("xray.default.labels")
    @DefaultValue("automation,e2e,cucumber")
    String defaultLabels();
}
