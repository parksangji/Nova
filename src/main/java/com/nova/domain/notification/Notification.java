package com.nova.domain.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
//noinspection SqlResolve
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

    @Column(length = 1000)
    private String failureReason;

    @SuppressWarnings("unused")
    @CreatedDate
    private LocalDateTime createdAt;

    @SuppressWarnings("unused")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Notification(String recipient, String content, NotificationType type) {
        this.recipient = recipient;
        this.content = content;
        this.type = type;
        this.status = NotificationStatus.PENDING;
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.failureReason = null;
    }

    public void markAsFailed(String failureReason) {
        this.status = NotificationStatus.FAILED;
        this.retryCount++;
        this.failureReason = failureReason;
    }
}
