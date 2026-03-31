package com.nova.service.notification.sender;

import com.nova.config.AppKakaoProperties;
import com.nova.domain.notification.Notification;
import com.nova.domain.notification.NotificationType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoNotificationSender implements NotificationSender {

    private final RestClient kakaoRestClient;
    private final AppKakaoProperties appKakaoProperties;

    @Override
    public void send(Notification notification) {
        if (!appKakaoProperties.enabled()) {
            throw new IllegalStateException("Kakao delivery is disabled. Set app.kakao.enabled=true.");
        }
        if (isBlank(appKakaoProperties.webhookUrl())) {
            throw new IllegalStateException("Kakao webhook URL is missing.");
        }

        log.info("Sending KakaoTalk notification to: {}", notification.getRecipient());
        kakaoRestClient.post()
                .uri(appKakaoProperties.webhookUrl())
                .headers(headers -> {
                    if (!isBlank(appKakaoProperties.authToken())) {
                        headers.setBearerAuth(appKakaoProperties.authToken());
                    }
                })
                .body(Map.of(
                        "recipientPhoneNumber", notification.getRecipient(),
                        "content", notification.getContent(),
                        "senderKey", nullToEmpty(appKakaoProperties.senderKey()),
                        "templateCode", nullToEmpty(appKakaoProperties.templateCode())
                ))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.KAKAO;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
