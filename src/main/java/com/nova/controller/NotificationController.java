package com.nova.controller;

import com.nova.controller.dto.NotificationRequest;
import com.nova.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Long> requestNotification(
            @RequestBody @Valid NotificationRequest request
    ) {
        Long notificationId = notificationService.createNotification(
                request.recipient(),
                request.content(),
                request.type()
        );

        notificationService.sendNotification(notificationId);
        return ResponseEntity.ok(notificationId);
    }
}
