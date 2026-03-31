package com.nova.service;

import com.nova.config.AppDispatchProperties;
import com.nova.controller.dto.DailyNotificationScheduleRequest;
import com.nova.controller.dto.DailyNotificationScheduleResponse;
import com.nova.domain.DailyNotificationSchedule;
import com.nova.domain.Notification;
import com.nova.domain.NotificationStatus;
import com.nova.domain.NotificationType;
import com.nova.repository.DailyNotificationScheduleRepository;
import com.nova.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyNotificationScheduleService {

    private final DailyNotificationScheduleRepository dailyNotificationScheduleRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationDispatchService notificationDispatchService;
    private final AppDispatchProperties appDispatchProperties;

    @Transactional
    public DailyNotificationScheduleResponse getSchedule() {
        DailyNotificationSchedule schedule = getOrCreateDefaultSchedule();
        return toResponse(schedule);
    }

    @Transactional
    public DailyNotificationScheduleResponse updateSchedule(DailyNotificationScheduleRequest request) {
        DailyNotificationSchedule schedule = getOrCreateDefaultSchedule();
        schedule.update(
                request.enabled(),
                request.recipient(),
                request.content(),
                request.type()
        );
        return toResponse(schedule);
    }

    @Transactional
    public void triggerScheduledDispatch() {
        DailyNotificationSchedule schedule = getOrCreateDefaultSchedule();
        if (!schedule.isEnabled()) {
            log.info("Daily notification schedule is disabled. Skipping dispatch.");
            return;
        }

        LocalDate today = LocalDate.now(ZoneId.of(appDispatchProperties.zone()));
        if (today.equals(schedule.getLastDispatchedOn())) {
            log.info("Daily notification already dispatched for {}", today);
            return;
        }

        dispatch(schedule, today);
    }

    @Transactional
    public void triggerNow() {
        DailyNotificationSchedule schedule = getOrCreateDefaultSchedule();
        if (!schedule.isEnabled()) {
            log.info("Manual trigger requested while schedule disabled. Dispatching anyway.");
        }
        dispatch(schedule, null);
    }

    private void dispatch(DailyNotificationSchedule schedule, LocalDate dispatchedOn) {
        Notification notification = Notification.builder()
                .recipient(schedule.getRecipient())
                .content(schedule.getContent())
                .type(schedule.getType())
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        NotificationStatus status = notificationDispatchService.sendNotification(savedNotification.getId());

        if (status == NotificationStatus.SENT && dispatchedOn != null) {
            schedule.markDispatched(dispatchedOn);
        }
    }

    private DailyNotificationSchedule getOrCreateDefaultSchedule() {
        return dailyNotificationScheduleRepository.findById(DailyNotificationSchedule.DEFAULT_ID)
                .orElseGet(() -> dailyNotificationScheduleRepository.save(
                        DailyNotificationSchedule.builder()
                                .id(DailyNotificationSchedule.DEFAULT_ID)
                                .enabled(true)
                                .recipient(appDispatchProperties.recipientPhone())
                                .content(appDispatchProperties.defaultBody())
                                .type(NotificationType.KAKAO)
                                .build()
                ));
    }

    private DailyNotificationScheduleResponse toResponse(DailyNotificationSchedule schedule) {
        return new DailyNotificationScheduleResponse(
                schedule.isEnabled(),
                schedule.getRecipient(),
                schedule.getContent(),
                schedule.getType(),
                appDispatchProperties.cron(),
                appDispatchProperties.zone(),
                schedule.getLastDispatchedOn()
        );
    }
}
