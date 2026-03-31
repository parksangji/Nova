package com.nova.service.notification;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class DailyNotificationSchedulerTest {

    @Test
    void delegatesToScheduleService() {
        DailyNotificationScheduleService service = mock(DailyNotificationScheduleService.class);
        DailyNotificationScheduler scheduler = new DailyNotificationScheduler(service);

        scheduler.sendDailyNotification();

        verify(service).triggerScheduledDispatch();
    }
}
