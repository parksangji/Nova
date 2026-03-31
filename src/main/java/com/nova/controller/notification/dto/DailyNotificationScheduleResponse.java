package com.nova.controller.notification.dto;

import com.nova.domain.notification.NotificationType;
import java.time.LocalDate;

public record DailyNotificationScheduleResponse(
        boolean enabled,
        String recipient,
        String content,
        NotificationType type,
        String cron,
        String zone,
        LocalDate lastDispatchedOn
) {
}
