package com.nova.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationAsyncDispatchService {

    private final NotificationDispatchService notificationDispatchService;

    @Async
    public void sendNotificationAsync(Long id) {
        notificationDispatchService.sendNotification(id);
    }
}
