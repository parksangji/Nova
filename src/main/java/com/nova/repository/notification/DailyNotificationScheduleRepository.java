package com.nova.repository.notification;

import com.nova.domain.notification.DailyNotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyNotificationScheduleRepository extends JpaRepository<DailyNotificationSchedule, Long> {
}
