package GRWM.backend.repository;

import GRWM.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByPersonalPlannerIdAndStartDateTimeBetweenOrderByStartDateTimeAsc(Long plannerId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Schedule> findByPersonalPlannerIdAndTitleContainingOrLocationContainingOrMemoContaining(Long plannerId, String titleKeyword, String locationKeyword, String memoKeyword);
}
