package com.ecommerce.config;

import org.aeonbits.owner.Config;

/**
 * Configuration interface for test settings.
 * Uses Owner library for configuration management.
 */
@Config.Sources({
    "classpath:test.properties",
    "system:properties",
    "system:env"
})
public interface TestConfig extends Config {

    @Key("base.url")
    @DefaultValue("http://localhost:3000")
    String baseUrl();

    @Key("browser.type")
    @DefaultValue("chromium")
    String browserType();

    @Key("headless")
    @DefaultValue("false")
    boolean isHeadless();

    @Key("timeout.default")
    @DefaultValue("10000")
    int defaultTimeout();

    @Key("timeout.long")
    @DefaultValue("30000")
    int longTimeout();

    @Key("viewport.width")
    @DefaultValue("1920")
    int viewportWidth();

    @Key("viewport.height")
    @DefaultValue("1080")
    int viewportHeight();
}
