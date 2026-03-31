package com.nova.repository;

import com.nova.domain.DailyNotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyNotificationScheduleRepository extends JpaRepository<DailyNotificationSchedule, Long> {
}
