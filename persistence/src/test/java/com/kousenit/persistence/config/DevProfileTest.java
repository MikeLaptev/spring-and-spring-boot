package com.kousenit.persistence.config;

import com.kousenit.persistence.config.ProfileConfig.ApplicationInfo;
import com.kousenit.persistence.config.ProfileConfig.DatabaseInfo;
import com.kousenit.persistence.config.ProfileConfig.FeatureToggle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class DevProfileTest {
    private static final Logger logger = LoggerFactory.getLogger(DevProfileTest.class);

    @Autowired
    private DatabaseInfo databaseInfo;

    @Autowired
    private ApplicationInfo applicationInfo;

    @Autowired
    private List<FeatureToggle> features;

    @Test
    void testDevProfileConfiguration() {
        // Test database configuration
        assertEquals("H2", databaseInfo.type());
        assertEquals("In-Memory", databaseInfo.location());
        assertEquals("Development", databaseInfo.purpose());
        assertTrue(databaseInfo.consoleEnabled());

        logger.info("Dev Database Info: {}", databaseInfo);
    }

    @Test
    void testApplicationInfo() {
        assertEquals("Spring Data JPA Persistence Demo", applicationInfo.name());
        assertEquals("dev", applicationInfo.environment());
        assertNotNull(applicationInfo.description());

        logger.info("Application Info: {}", applicationInfo);
    }

    @Test
    void testDevFeatures() {
        assertFalse(features.isEmpty());
        
        // Should have h2-console and debug features in dev profile
        boolean hasH2Console = features.stream()
                .anyMatch(f -> "h2-console".equals(f.name()) && f.enabled());
        boolean hasDebugLogging = features.stream()
                .anyMatch(f -> "debug-logging".equals(f.name()) && f.enabled());

        assertTrue(hasH2Console, "Dev profile should have H2 console enabled");
        assertTrue(hasDebugLogging, "Dev profile should have debug logging enabled");

        features.forEach(feature -> logger.info("Dev Feature: {}", feature));
    }
}