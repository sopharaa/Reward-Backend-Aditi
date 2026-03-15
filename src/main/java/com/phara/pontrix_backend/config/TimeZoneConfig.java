package com.phara.pontrix_backend.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {
    private static final TimeZone BANGKOK_TIME_ZONE = TimeZone.getTimeZone("Asia/Bangkok");
    private static final Logger log = LoggerFactory.getLogger(TimeZoneConfig.class);

    @PostConstruct
    void configureDefaultTimeZone() {
        TimeZone.setDefault(BANGKOK_TIME_ZONE);
        log.info("Default timezone set to {}", BANGKOK_TIME_ZONE.getID());
    }
}

