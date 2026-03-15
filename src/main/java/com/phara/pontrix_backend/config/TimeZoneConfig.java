package com.phara.pontrix_backend.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.TimeZone;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "bangkokDateTimeProvider")
public class TimeZoneConfig {
    private static final ZoneId BANGKOK_ZONE_ID = ZoneId.of("Asia/Bangkok");
    private static final TimeZone BANGKOK_TIME_ZONE = TimeZone.getTimeZone(BANGKOK_ZONE_ID);
    private static final Logger log = LoggerFactory.getLogger(TimeZoneConfig.class);

    @PostConstruct
    void configureDefaultTimeZone() {
        TimeZone.setDefault(BANGKOK_TIME_ZONE);
        log.info("Default timezone set to {}", BANGKOK_TIME_ZONE.getID());
    }

    @Bean
    ZoneId bangkokZoneId() {
        return BANGKOK_ZONE_ID;
    }

    @Bean
    Clock bangkokClock(ZoneId bangkokZoneId) {
        return Clock.system(bangkokZoneId);
    }

    @Bean
    DateTimeProvider bangkokDateTimeProvider(Clock bangkokClock) {
        return () -> Optional.of(OffsetDateTime.now(bangkokClock));
    }
}
