package com.nova.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.dispatch")
public record AppDispatchProperties(
        String cron,
        String zone,
        String defaultSubject,
        String defaultBody,
        String recipientPhone,
        String recipientEmail
) {
}
