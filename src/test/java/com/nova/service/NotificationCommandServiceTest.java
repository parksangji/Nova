package com.nova.service;

import com.nova.controller.dto.NotificationCreateRequest;
import com.nova.domain.Notification;
import com.nova.domain.NotificationType;
import com.nova.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationCommandServiceTest {

    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);
    private final NotificationAsyncDispatchService notificationAsyncDispatchService = mock(NotificationAsyncDispatchService.class);
    private final NotificationCommandService notificationCommandService =
            new NotificationCommandService(notificationRepository, notificationAsyncDispatchService);

    @Test
    void savesNotificationAndDispatchesAsync() {
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("content")
                .type(NotificationType.KAKAO)
                .build();
        ReflectionTestUtils.setField(notification, "id", 1L);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        doNothing().when(notificationAsyncDispatchService).sendNotificationAsync(any(Long.class));

        Long notificationId = notificationCommandService.requestNotification(
                new NotificationCreateRequest("01084627902", "content", NotificationType.KAKAO)
        );

        assertThat(notificationId).isEqualTo(1L);
        verify(notificationRepository).save(any(Notification.class));
        verify(notificationAsyncDispatchService).sendNotificationAsync(1L);
    }
}
