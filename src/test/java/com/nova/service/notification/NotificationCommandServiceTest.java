package com.nova.service.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nova.controller.notification.dto.NotificationCreateRequest;
import com.nova.domain.notification.Notification;
import com.nova.domain.notification.NotificationType;
import com.nova.repository.notification.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
