package com.nova.service.sender;

import com.nova.config.AppKakaoProperties;
import com.nova.domain.Notification;
import com.nova.domain.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KakaoNotificationSenderTest {

    private final RestClient restClient = RestClient.builder().build();

    @Test
    void throwsWhenKakaoDeliveryDisabled() {
        KakaoNotificationSender sender = new KakaoNotificationSender(
                restClient,
                new AppKakaoProperties(false, "https://example.com", "token", "sender", "template")
        );
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("hello")
                .type(NotificationType.KAKAO)
                .build();

        assertThatThrownBy(() -> sender.send(notification))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("disabled");
    }

    @Test
    void throwsWhenWebhookUrlMissing() {
        KakaoNotificationSender sender = new KakaoNotificationSender(
                restClient,
                new AppKakaoProperties(true, "", "token", "sender", "template")
        );
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("hello")
                .type(NotificationType.KAKAO)
                .build();

        assertThatThrownBy(() -> sender.send(notification))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("webhook URL is missing");
    }
}
