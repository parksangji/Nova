package com.nova.service.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nova.config.AppDispatchProperties;
import com.nova.controller.notification.dto.DailyNotificationScheduleRequest;
import com.nova.controller.notification.dto.DailyNotificationScheduleResponse;
import com.nova.domain.notification.DailyNotificationSchedule;
import com.nova.domain.notification.Notification;
import com.nova.domain.notification.NotificationStatus;
import com.nova.domain.notification.NotificationType;
import com.nova.repository.notification.DailyNotificationScheduleRepository;
import com.nova.repository.notification.NotificationRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DailyNotificationScheduleServiceTest {

    private final DailyNotificationScheduleRepository dailyNotificationScheduleRepository =
            mock(DailyNotificationScheduleRepository.class);
    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);
    private final NotificationDispatchService notificationDispatchService = mock(NotificationDispatchService.class);
    private final AppDispatchProperties appDispatchProperties =
            new AppDispatchProperties("0 0 9 * * *", "Asia/Seoul", "Daily Message", "Morning", "01084627902", "test@example.com");

    private DailyNotificationScheduleService dailyNotificationScheduleService;

    @BeforeEach
    void setUp() {
        dailyNotificationScheduleService = new DailyNotificationScheduleService(
                dailyNotificationScheduleRepository,
                notificationRepository,
                notificationDispatchService,
                appDispatchProperties
        );
    }

    @Test
    void createsDefaultScheduleWhenMissing() {
        when(dailyNotificationScheduleRepository.findById(DailyNotificationSchedule.DEFAULT_ID)).thenReturn(Optional.empty());
        when(dailyNotificationScheduleRepository.save(any(DailyNotificationSchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DailyNotificationScheduleResponse response = dailyNotificationScheduleService.getSchedule();

        assertThat(response.enabled()).isTrue();
        assertThat(response.recipient()).isEqualTo("01084627902");
        assertThat(response.content()).isEqualTo("Morning");
        assertThat(response.type()).isEqualTo(NotificationType.KAKAO);
    }

    @Test
    void updatesExistingSchedule() {
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .id(DailyNotificationSchedule.DEFAULT_ID)
                .enabled(true)
                .recipient("01084627902")
                .content("before")
                .type(NotificationType.KAKAO)
                .build();
        when(dailyNotificationScheduleRepository.findById(DailyNotificationSchedule.DEFAULT_ID))
                .thenReturn(Optional.of(schedule));

        DailyNotificationScheduleResponse response = dailyNotificationScheduleService.updateSchedule(
                new DailyNotificationScheduleRequest(false, "test@example.com", "after", NotificationType.EMAIL)
        );

        assertThat(response.enabled()).isFalse();
        assertThat(response.recipient()).isEqualTo("test@example.com");
        assertThat(response.content()).isEqualTo("after");
        assertThat(response.type()).isEqualTo(NotificationType.EMAIL);
    }

    @Test
    void skipsScheduledDispatchWhenDisabled() {
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .id(DailyNotificationSchedule.DEFAULT_ID)
                .enabled(false)
                .recipient("01084627902")
                .content("before")
                .type(NotificationType.KAKAO)
                .build();
        when(dailyNotificationScheduleRepository.findById(DailyNotificationSchedule.DEFAULT_ID))
                .thenReturn(Optional.of(schedule));

        dailyNotificationScheduleService.triggerScheduledDispatch();

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void skipsScheduledDispatchWhenAlreadySentToday() {
        LocalDate today = LocalDate.now(ZoneId.of(appDispatchProperties.zone()));
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .id(DailyNotificationSchedule.DEFAULT_ID)
                .enabled(true)
                .recipient("01084627902")
                .content("before")
                .type(NotificationType.KAKAO)
                .lastDispatchedOn(today)
                .build();
        when(dailyNotificationScheduleRepository.findById(DailyNotificationSchedule.DEFAULT_ID))
                .thenReturn(Optional.of(schedule));

        dailyNotificationScheduleService.triggerScheduledDispatch();

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void dispatchesAndMarksDateWhenSendSucceeds() {
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .id(DailyNotificationSchedule.DEFAULT_ID)
                .enabled(true)
                .recipient("01084627902")
                .content("before")
                .type(NotificationType.KAKAO)
                .build();
        Notification savedNotification = Notification.builder()
                .recipient("01084627902")
                .content("before")
                .type(NotificationType.KAKAO)
                .build();
        ReflectionTestUtils.setField(savedNotification, "id", 1L);
        when(dailyNotificationScheduleRepository.findById(DailyNotificationSchedule.DEFAULT_ID))
                .thenReturn(Optional.of(schedule));
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);
        when(notificationDispatchService.sendNotification(1L)).thenReturn(NotificationStatus.SENT);

        dailyNotificationScheduleService.triggerScheduledDispatch();

        assertThat(schedule.getLastDispatchedOn()).isEqualTo(LocalDate.now(ZoneId.of(appDispatchProperties.zone())));
    }

    @Test
    void manualTriggerDispatchesEvenWhenDisabled() {
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .id(DailyNotificationSchedule.DEFAULT_ID)
                .enabled(false)
                .recipient("01084627902")
                .content("manual")
                .type(NotificationType.KAKAO)
                .build();
        Notification savedNotification = Notification.builder()
                .recipient("01084627902")
                .content("manual")
                .type(NotificationType.KAKAO)
                .build();
        ReflectionTestUtils.setField(savedNotification, "id", 1L);
        when(dailyNotificationScheduleRepository.findById(DailyNotificationSchedule.DEFAULT_ID))
                .thenReturn(Optional.of(schedule));
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);
        when(notificationDispatchService.sendNotification(1L)).thenReturn(NotificationStatus.SENT);

        dailyNotificationScheduleService.triggerNow();

        verify(notificationRepository).save(any(Notification.class));
    }
}
