package com.nova.service;

import com.nova.domain.NotificationType;
import com.nova.service.sender.NotificationSender;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
