package com.nova.service.sender;

import com.nova.domain.Notification;
import com.nova.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void send(Notification notification) {
        log.info("Attempting to send Email to: {}", notification.getRecipient());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getRecipient());
        message.setSubject("Nova Daily Notification");
        message.setText(notification.getContent());
        message.setFrom(username);

        mailSender.send(message);
        log.info("Email sent successfully to: {}", notification.getRecipient());
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.EMAIL;
    }
}
