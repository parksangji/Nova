package com.nova.service.sender;

import com.nova.domain.Notification;
import com.nova.domain.NotificationType;

public interface NotificationSender {
    void send(Notification notification);

    boolean supports(NotificationType type);
}
