package com.nova.service;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DailyNotificationSchedulerTest {

    @Test
    void delegatesToScheduleService() {
        DailyNotificationScheduleService service = mock(DailyNotificationScheduleService.class);
        DailyNotificationScheduler scheduler = new DailyNotificationScheduler(service);

        scheduler.sendDailyNotification();

        verify(service).triggerScheduledDispatch();
    }
}
