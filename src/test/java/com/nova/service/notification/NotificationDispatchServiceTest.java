package com.nova.service.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nova.domain.notification.Notification;
import com.nova.domain.notification.NotificationStatus;
import com.nova.domain.notification.NotificationType;
import com.nova.repository.notification.NotificationRepository;
import com.nova.service.notification.sender.NotificationSender;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class NotificationDispatchServiceTest {

    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);
    private final NotificationSenderRouter notificationSenderRouter = mock(NotificationSenderRouter.class);
    private final NotificationDispatchService notificationDispatchService =
            new NotificationDispatchService(notificationRepository, notificationSenderRouter);

    @Test
    void sendsAndMarksNotificationAsSent() {
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("dispatch test")
                .type(NotificationType.KAKAO)
                .build();
        NotificationSender sender = mock(NotificationSender.class);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationSenderRouter.getSender(NotificationType.KAKAO)).thenReturn(sender);

        NotificationStatus status = notificationDispatchService.sendNotification(1L);

        assertThat(status).isEqualTo(NotificationStatus.SENT);
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        verify(sender).send(notification);
    }

    @Test
    void marksNotificationAsFailedWhenSenderThrows() {
        Notification notification = Notification.builder()
                .recipient("01084627902")
                .content("dispatch test")
                .type(NotificationType.KAKAO)
                .build();
        NotificationSender sender = mock(NotificationSender.class);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationSenderRouter.getSender(NotificationType.KAKAO)).thenReturn(sender);
        doThrow(new IllegalStateException("send failed")).when(sender).send(any(Notification.class));

        NotificationStatus status = notificationDispatchService.sendNotification(1L);

        assertThat(status).isEqualTo(NotificationStatus.FAILED);
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
        assertThat(notification.getFailureReason()).contains("send failed");
    }

    @Test
    void throwsWhenNotificationDoesNotExist() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationDispatchService.sendNotification(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Notification not found");
    }
}
