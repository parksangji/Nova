package com.nova.controller.notification.dto;

import com.nova.domain.notification.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DailyNotificationScheduleRequest(
        boolean enabled,
        @NotBlank String recipient,
        @NotBlank String content,
        @NotNull NotificationType type
) {
}
