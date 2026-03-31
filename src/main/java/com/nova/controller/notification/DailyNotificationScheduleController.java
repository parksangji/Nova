package com.nova.controller.notification;

import com.nova.controller.notification.dto.DailyNotificationScheduleRequest;
import com.nova.controller.notification.dto.DailyNotificationScheduleResponse;
import com.nova.service.notification.DailyNotificationScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/daily-notifications")
@RequiredArgsConstructor
public class DailyNotificationScheduleController {

    private final DailyNotificationScheduleService dailyNotificationScheduleService;

    @GetMapping
    public ResponseEntity<DailyNotificationScheduleResponse> getSchedule() {
        return ResponseEntity.ok(dailyNotificationScheduleService.getSchedule());
    }

    @PutMapping
    public ResponseEntity<DailyNotificationScheduleResponse> updateSchedule(
            @RequestBody @Valid DailyNotificationScheduleRequest request
    ) {
        return ResponseEntity.ok(dailyNotificationScheduleService.updateSchedule(request));
    }

    @PostMapping("/trigger")
    public ResponseEntity<DailyNotificationScheduleResponse> triggerNow() {
        dailyNotificationScheduleService.triggerNow();
        return ResponseEntity.ok(dailyNotificationScheduleService.getSchedule());
    }
}
