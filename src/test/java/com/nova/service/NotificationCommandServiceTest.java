package com.nova.service;

import com.nova.controller.dto.NotificationCreateRequest;
import com.nova.domain.Notification;
import com.nova.domain.NotificationType;
import com.nova.repository.NotificationRepository;
import com.nova.service.event.NotificationSavedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationCommandServiceTest {

    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
    private final NotificationCommandService notificationCommandService =
            new NotificationCommandService(notificationRepository, eventPublisher);

    @Test
    void savesNotificationAndPublishesEvent() {
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("content")
                .type(NotificationType.KAKAO)
                .build();
        ReflectionTestUtils.setField(notification, "id", 1L);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Long notificationId = notificationCommandService.requestNotification(
                new NotificationCreateRequest("01084627902", "content", NotificationType.KAKAO)
        );

        assertThat(notificationId).isEqualTo(1L);
        verify(notificationRepository).save(any(Notification.class));
        verify(eventPublisher).publishEvent(new NotificationSavedEvent(1L));
    }
}
