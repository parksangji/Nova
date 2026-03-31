package com.nova.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
//noinspection SqlResolve
@Table(name = "daily_notification_schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DailyNotificationSchedule {

    public static final Long DEFAULT_ID = 1L;

    @Id
    private Long id;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationType type;

    private LocalDate lastDispatchedOn;

    @SuppressWarnings("unused")
    @CreatedDate
    private LocalDateTime createdAt;

    @SuppressWarnings("unused")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @SuppressWarnings("unused")
    @Builder
    public DailyNotificationSchedule(
            Long id,
            boolean enabled,
            String recipient,
            String content,
            NotificationType type,
            LocalDate lastDispatchedOn
    ) {
        this.id = id == null ? DEFAULT_ID : id;
        this.enabled = enabled;
        this.recipient = recipient;
        this.content = content;
        this.type = type;
        this.lastDispatchedOn = lastDispatchedOn;
    }

    public void update(boolean enabled, String recipient, String content, NotificationType type) {
        this.enabled = enabled;
        this.recipient = recipient;
        this.content = content;
        this.type = type;
    }

    public void markDispatched(LocalDate dispatchedOn) {
        this.lastDispatchedOn = dispatchedOn;
    }
}
