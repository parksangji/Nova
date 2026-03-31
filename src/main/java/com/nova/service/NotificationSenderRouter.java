package com.nova.service;

import com.nova.domain.NotificationType;
import com.nova.service.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationSenderRouter {

    private final List<NotificationSender> notificationSenders;

    public NotificationSender getSender(NotificationType type) {
        return notificationSenders.stream()
                .filter(sender -> sender.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No provider found for type: " + type));
    }
}
