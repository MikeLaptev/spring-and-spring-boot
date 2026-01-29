package com.kousenit.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfig {
    private static final Logger logger = LoggerFactory.getLogger(ProfileConfig.class);

    @Value("${app.name}")
    private String appName;

    @Value("${app.environment}")
    private String environment;

    @Value("${app.description}")
    private String description;

    /**
     * Bean that's only active in development profile
     */
    @Bean
    @Profile("dev")
    public DatabaseInfo developmentDatabaseInfo() {
        logger.info("Creating development database info bean");
        return new DatabaseInfo("H2", "In-Memory", "Development", true);
    }

    /**
     * Bean that's only active in test profile
     */
    @Bean
    @Profile("test")
    public DatabaseInfo testDatabaseInfo() {
        logger.info("Creating test database info bean");
        return new DatabaseInfo("H2", "In-Memory", "Testing", false);
    }

    /**
     * Bean that's only active in production profile
     */
    @Bean
    @Profile("prod")
    public DatabaseInfo productionDatabaseInfo() {
        logger.info("Creating production database info bean");
        return new DatabaseInfo("PostgreSQL", "Docker Container", "Production", false);
    }

    /**
     * Bean that's active in development OR test profiles
     */
    @Bean
    @Profile({"dev", "test"})
    public FeatureToggle h2ConsoleFeature() {
        logger.info("Enabling H2 console feature for dev/test profiles");
        return new FeatureToggle("h2-console", true);
    }

    /**
     * Bean that's active in production profile
     */
    @Bean
    @Profile("prod")
    public FeatureToggle productionFeatures() {
        logger.info("Enabling production features");
        return new FeatureToggle("production-monitoring", true);
    }

    /**
     * Bean that's NOT active in production (using ! prefix)
     */
    @Bean
    @Profile("!prod")
    public FeatureToggle debugFeature() {
        logger.info("Enabling debug features for non-production environments");
        return new FeatureToggle("debug-logging", true);
    }

    /**
     * Information about the current application environment
     */
    @Bean
    public ApplicationInfo applicationInfo() {
        return new ApplicationInfo(appName, environment, description);
    }

    // Records for profile-specific configuration
    public record DatabaseInfo(String type, String location, String purpose, boolean consoleEnabled) {}

    public record FeatureToggle(String name, boolean enabled) {}

    public record ApplicationInfo(String name, String environment, String description) {}
}