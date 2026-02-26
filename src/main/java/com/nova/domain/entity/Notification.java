package com.nova.domain.entity;

import com.nova.domain.constants.NotificationStatus;
import com.nova.domain.constants.NotificationType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private int retryCount;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Notification(String recipient, String content, NotificationType type) {
        this.recipient = recipient;
        this.content = content;
        this.type = type;
        this.status = NotificationStatus.PENDING;
    }

    public void updateStatus(NotificationStatus status) {
        this.status = status;
        if (status == NotificationStatus.FAILED) this.retryCount++;
    }
}
