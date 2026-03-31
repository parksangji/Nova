package com.nova.service.notification.sender;

import com.nova.domain.notification.Notification;
import com.nova.domain.notification.NotificationType;

public interface NotificationSender {
    void send(Notification notification);

    boolean supports(NotificationType type);
}
