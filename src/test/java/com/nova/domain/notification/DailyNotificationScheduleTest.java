package com.nova.domain.notification;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DailyNotificationScheduleTest {

    @Test
    void defaultsIdToStaticDefaultId() {
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .enabled(true)
                .recipient("01084627902")
                .content("good morning")
                .type(NotificationType.KAKAO)
                .build();

        assertThat(schedule.getId()).isEqualTo(DailyNotificationSchedule.DEFAULT_ID);
        assertThat(schedule.isEnabled()).isTrue();
    }

    @Test
    void updatesScheduleFields() {
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .id(1L)
                .enabled(true)
                .recipient("01084627902")
                .content("before")
                .type(NotificationType.KAKAO)
                .build();

        schedule.update(false, "parksangji1109@gmail.com", "after", NotificationType.EMAIL);

        assertThat(schedule.isEnabled()).isFalse();
        assertThat(schedule.getRecipient()).isEqualTo("parksangji1109@gmail.com");
        assertThat(schedule.getContent()).isEqualTo("after");
        assertThat(schedule.getType()).isEqualTo(NotificationType.EMAIL);
    }

    @Test
    void marksDispatchDate() {
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .id(1L)
                .enabled(true)
                .recipient("01084627902")
                .content("before")
                .type(NotificationType.KAKAO)
                .build();

        LocalDate today = LocalDate.of(2026, 3, 31);
        schedule.markDispatched(today);

        assertThat(schedule.getLastDispatchedOn()).isEqualTo(today);
    }
}
