package com.nova.service.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nova.domain.notification.NotificationType;
import com.nova.service.notification.sender.NotificationSender;
import java.util.List;
import org.junit.jupiter.api.Test;

class NotificationSenderRouterTest {

    @Test
    void returnsSenderForMatchingType() {
        NotificationSender emailSender = mock(NotificationSender.class);
        NotificationSender kakaoSender = mock(NotificationSender.class);
        when(emailSender.supports(NotificationType.EMAIL)).thenReturn(true);
        when(kakaoSender.supports(NotificationType.EMAIL)).thenReturn(false);

        NotificationSenderRouter router = new NotificationSenderRouter(List.of(kakaoSender, emailSender));

        assertThat(router.getSender(NotificationType.EMAIL)).isSameAs(emailSender);
    }

    @Test
    void throwsWhenNoSenderMatches() {
        NotificationSender kakaoSender = mock(NotificationSender.class);
        when(kakaoSender.supports(NotificationType.EMAIL)).thenReturn(false);

        NotificationSenderRouter router = new NotificationSenderRouter(List.of(kakaoSender));

        assertThatThrownBy(() -> router.getSender(NotificationType.EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No provider found");
    }
}
