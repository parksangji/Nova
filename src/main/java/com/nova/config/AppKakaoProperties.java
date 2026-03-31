package com.nova.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kakao")
public record AppKakaoProperties(
        boolean enabled,
        String webhookUrl,
        String authToken,
        String senderKey,
        String templateCode
) {
}
