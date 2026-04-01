package com.nova.service.sender;

import com.nova.domain.Notification;
import com.nova.domain.NotificationType;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

class EmailNotificationSenderTest {

    @Test
    void sendsMailMessage() throws Exception {
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        EmailNotificationSender sender = new EmailNotificationSender(javaMailSender);
        setField(sender, "host", "smtp.gmail.com");
        Field usernameField = EmailNotificationSender.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(sender, "sender@example.com");
        setField(sender, "password", "app-password");

        Notification notification = Notification.builder()
                .recipient("receiver@example.com")
                .content("hello")
                .type(NotificationType.EMAIL)
                .build();

        sender.send(notification);

        verify(javaMailSender).send(any(org.springframework.mail.SimpleMailMessage.class));
    }

    @Test
    void failsFastWhenUsernameIsBlank() throws Exception {
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        EmailNotificationSender sender = new EmailNotificationSender(javaMailSender);
        setField(sender, "host", "smtp.gmail.com");
        setField(sender, "username", "");
        setField(sender, "password", "app-password");

        assertThatThrownBy(() -> sender.validateMailConfiguration())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("spring.mail.username is blank");
    }

    @Test
    void failsFastWhenPasswordIsBlank() throws Exception {
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        EmailNotificationSender sender = new EmailNotificationSender(javaMailSender);
        setField(sender, "host", "smtp.gmail.com");
        setField(sender, "username", "sender@example.com");
        setField(sender, "password", "");

        assertThatThrownBy(() -> sender.validateMailConfiguration())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("spring.mail.password is blank");
    }

    @Test
    void setsExpectedMailFields() throws Exception {
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        EmailNotificationSender sender = new EmailNotificationSender(javaMailSender);
        setField(sender, "host", "smtp.gmail.com");
        setField(sender, "username", "sender@example.com");
        setField(sender, "password", "app-password");

        Notification notification = Notification.builder()
                .recipient("receiver@example.com")
                .content("hello")
                .type(NotificationType.EMAIL)
                .build();

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        doNothing().when(javaMailSender).send(captor.capture());

        sender.send(notification);

        assertThat(captor.getValue().getFrom()).isEqualTo("sender@example.com");
        assertThat(captor.getValue().getTo()).containsExactly("receiver@example.com");
        assertThat(captor.getValue().getText()).isEqualTo("hello");
    }

    private void setField(EmailNotificationSender sender, String fieldName, String value) throws Exception {
        Field field = EmailNotificationSender.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(sender, value);
    }
}
