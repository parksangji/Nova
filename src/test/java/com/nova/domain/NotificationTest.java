package com.nova.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {

    @Test
    void createsPendingNotificationByDefault() {
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("test message")
                .type(NotificationType.KAKAO)
                .build();

        assertThat(notification.getRecipient()).isEqualTo("01084627902");
        assertThat(notification.getContent()).isEqualTo("test message");
        assertThat(notification.getType()).isEqualTo(NotificationType.KAKAO);
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.PENDING);
        assertThat(notification.getRetryCount()).isZero();
        assertThat(notification.getFailureReason()).isNull();
    }

    @Test
    void marksNotificationAsSent() {
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("test message")
                .type(NotificationType.KAKAO)
                .build();

        notification.markAsFailed("failed once");
        notification.markAsSent();

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        assertThat(notification.getFailureReason()).isNull();
        assertThat(notification.getRetryCount()).isEqualTo(1);
    }

    @Test
    void marksNotificationAsFailedAndIncrementsRetryCount() {
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("test message")
                .type(NotificationType.KAKAO)
                .build();

        notification.markAsFailed("boom");

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
        assertThat(notification.getRetryCount()).isEqualTo(1);
        assertThat(notification.getFailureReason()).isEqualTo("boom");
    }
}
