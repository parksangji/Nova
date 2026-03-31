package com.nova.repository;

import com.nova.domain.DailyNotificationSchedule;
import com.nova.domain.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:daily-schedule-repository-test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
@Transactional
class DailyNotificationScheduleRepositoryTest {

    @Autowired
    private DailyNotificationScheduleRepository dailyNotificationScheduleRepository;

    @Test
    void savesSchedule() {
        DailyNotificationSchedule schedule = DailyNotificationSchedule.builder()
                .id(DailyNotificationSchedule.DEFAULT_ID)
                .enabled(true)
                .recipient("01084627902")
                .content("schedule repository test")
                .type(NotificationType.KAKAO)
                .build();

        DailyNotificationSchedule saved = dailyNotificationScheduleRepository.save(schedule);

        assertThat(saved.getId()).isEqualTo(DailyNotificationSchedule.DEFAULT_ID);
        assertThat(dailyNotificationScheduleRepository.findById(saved.getId())).contains(saved);
    }
}
