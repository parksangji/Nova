package com.nova.service.sender.impl;

import com.nova.domain.entity.Notification;
import com.nova.domain.constants.NotificationType;

public interface NotificationSender {
    void send(Notification notification);

    boolean supports(NotificationType type);
}
