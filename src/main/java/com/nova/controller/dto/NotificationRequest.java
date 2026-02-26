package com.nova.controller.dto;

import com.nova.domain.constants.NotificationType;
import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(
        @NotBlank String recipient,
        @NotBlank String content,
        NotificationType type
) {
}
