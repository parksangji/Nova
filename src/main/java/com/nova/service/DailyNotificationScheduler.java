package com.nova.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyNotificationScheduler {

    private final DailyNotificationScheduleService dailyNotificationScheduleService;

    @Scheduled(cron = "${app.dispatch.cron:0 0 9 * * *}", zone = "${app.dispatch.zone:Asia/Seoul}")
    public void sendDailyNotification() {
        log.info("Running daily notification scheduler");
        dailyNotificationScheduleService.triggerScheduledDispatch();
    }
}
