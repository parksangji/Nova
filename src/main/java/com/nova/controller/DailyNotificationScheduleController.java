package com.nova.controller;

import com.nova.controller.dto.DailyNotificationScheduleRequest;
import com.nova.controller.dto.DailyNotificationScheduleResponse;
import com.nova.service.DailyNotificationScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
