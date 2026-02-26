package com.nova.service;

import com.nova.domain.entity.Notification;
import com.nova.domain.repository.NotificationRepository;
import com.nova.domain.constants.NotificationStatus;
import com.nova.domain.constants.NotificationType;
import com.nova.service.sender.impl.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final List<NotificationSender> senders;

    @Transactional
    public Long createNotification(String recipient, String content, NotificationType type) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .content(content)
                .type(type)
                .build();
        repository.save(notification);
        return notification.getId();
    }

    @Async
    public void sendNotification(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found"));

        NotificationSender sender = senders.stream()
                .filter(s -> s.supports(notification.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No provider found"));

        try {
            sender.send(notification);
            notification.updateStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notification.updateStatus(NotificationStatus.FAILED);
        } finally {
            repository.save(notification);
        }
    }
}
