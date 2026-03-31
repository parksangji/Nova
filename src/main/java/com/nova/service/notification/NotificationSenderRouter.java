package com.nova.service.notification;

import com.nova.domain.notification.NotificationType;
import com.nova.service.notification.sender.NotificationSender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
