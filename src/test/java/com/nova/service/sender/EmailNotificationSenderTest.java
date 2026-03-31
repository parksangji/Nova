package com.nova.service.sender;

import com.nova.domain.Notification;
import com.nova.domain.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmailNotificationSenderTest {

    @Test
    void sendsMailMessage() throws Exception {
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        EmailNotificationSender sender = new EmailNotificationSender(javaMailSender);
        Field usernameField = EmailNotificationSender.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(sender, "sender@example.com");

        Notification notification = Notification.builder()
                .recipient("receiver@example.com")
                .content("hello")
                .type(NotificationType.EMAIL)
                .build();

        sender.send(notification);

        verify(javaMailSender).send(any(org.springframework.mail.SimpleMailMessage.class));
    }
}
