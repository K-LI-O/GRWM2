package GRWM.backend.repository.teamplanner;


import GRWM.backend.entity.teamplanner.TeamCategory;
import GRWM.backend.entity.teamplanner.TeamPlanner;
import GRWM.backend.entity.teamplanner.TeamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TeamScheduleRepository extends JpaRepository<TeamSchedule, Long> {
    // 플래너와 카테고리 객체로 스케줄 리스트 찾기
    List<TeamSchedule> findByTeamPlannerAndCategory(TeamPlanner planner, TeamCategory category);

    List<TeamSchedule> findByTeamPlannerIdAndStartTimeBetweenOrderByStartTimeAsc(Long plannerId, LocalDateTime startTime, LocalDateTime finishTime);
}
