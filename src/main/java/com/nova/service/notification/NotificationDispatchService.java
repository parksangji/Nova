package com.nova.service.notification;

import com.nova.domain.notification.Notification;
import com.nova.domain.notification.NotificationStatus;
import com.nova.repository.notification.NotificationRepository;
import com.nova.service.notification.sender.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDispatchService {

    private final NotificationRepository notificationRepository;
    private final NotificationSenderRouter notificationSenderRouter;

    @Transactional
    public NotificationStatus sendNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));

        NotificationSender notificationSender = notificationSenderRouter.getSender(notification.getType());

        try {
            notificationSender.send(notification);
            notification.markAsSent();
            return NotificationStatus.SENT;
        } catch (Exception exception) {
            log.error("Failed to dispatch notification {}: {}", id, exception.getMessage(), exception);
            notification.markAsFailed(exception.getMessage());
            return NotificationStatus.FAILED;
        }
    }

}
