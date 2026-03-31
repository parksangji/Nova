package com.nova.controller.notification;

import com.nova.controller.notification.dto.NotificationCreateRequest;
import com.nova.controller.notification.dto.NotificationCreateResponse;
import com.nova.service.notification.NotificationCommandService;
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

    private final NotificationCommandService notificationCommandService;

    @PostMapping
    public ResponseEntity<NotificationCreateResponse> requestNotification(
            @RequestBody @Valid NotificationCreateRequest request
    ) {
        Long notificationId = notificationCommandService.requestNotification(request);
        return ResponseEntity.ok(new NotificationCreateResponse(notificationId));
    }
}
