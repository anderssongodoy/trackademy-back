package com.trackademy.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase() {
        try {
            logger.info("Starting database initialization");

            jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\"");
            logger.debug("UUID extension created/verified");

            jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS \"pgcrypto\"");
            logger.debug("pgcrypto extension created/verified");

            logger.info("Database initialization completed successfully");
        } catch (Exception e) {
            logger.error("Error during database initialization", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
