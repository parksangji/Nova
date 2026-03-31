package com.nova.service.notification;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class NotificationAsyncDispatchServiceTest {

    @Test
    void delegatesAsyncDispatchToNotificationDispatchService() {
        NotificationDispatchService notificationDispatchService = mock(NotificationDispatchService.class);
        NotificationAsyncDispatchService notificationAsyncDispatchService =
                new NotificationAsyncDispatchService(notificationDispatchService);

        notificationAsyncDispatchService.sendNotificationAsync(1L);

        verify(notificationDispatchService).sendNotification(1L);
    }
}
