package com.nova.service;

import com.nova.domain.Notification;
import com.nova.domain.NotificationStatus;
import com.nova.repository.NotificationRepository;
import com.nova.service.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDispatchService {

    private final NotificationRepository notificationRepository;
    private final NotificationSenderRouter notificationSenderRouter;

    public NotificationStatus sendNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));

        NotificationSender notificationSender = notificationSenderRouter.getSender(notification.getType());

        try {
            notificationSender.send(notification);
            notification.markAsSent();
            notificationRepository.save(notification);
            return NotificationStatus.SENT;
        } catch (Exception exception) {
            log.error("Failed to dispatch notification {}: {}", id, exception.getMessage(), exception);
            notification.markAsFailed(exception.getMessage());
            notificationRepository.save(notification);
            return NotificationStatus.FAILED;
        }
    }

}
