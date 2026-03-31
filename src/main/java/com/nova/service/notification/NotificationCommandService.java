package com.nova.service.notification;

import com.nova.controller.notification.dto.NotificationCreateRequest;
import com.nova.domain.notification.Notification;
import com.nova.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final NotificationAsyncDispatchService notificationAsyncDispatchService;

    @Transactional
    public Long requestNotification(NotificationCreateRequest request) {
        Notification notification = Notification.builder()
                .recipient(request.recipient())
                .content(request.content())
                .type(request.type())
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        notificationAsyncDispatchService.sendNotificationAsync(savedNotification.getId());
        return savedNotification.getId();
    }
}
