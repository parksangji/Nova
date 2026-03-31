package com.nova.service.notification.sender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.nova.domain.notification.Notification;
import com.nova.domain.notification.NotificationType;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

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
