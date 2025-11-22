package GRWM.backend.repository.teamplanner;


import GRWM.backend.entity.teamplanner.TeamCategory;
import GRWM.backend.entity.teamplanner.TeamPlanner;
import GRWM.backend.entity.teamplanner.TeamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TeamScheduleRepository extends JpaRepository<TeamSchedule, Long> {
    // 플래너와 카테고리 객체로 스케줄 리스트 찾기
    List<TeamSchedule> findByTeamPlannerAndCategory(TeamPlanner planner, TeamCategory category);

    List<TeamSchedule> findByTeamPlannerIdAndStartTimeBetweenOrderByStartTimeAsc(Long plannerId, LocalDateTime startTime, LocalDateTime finishTime);

    // 키워드로 제목과 메모 검색하기
    List<TeamSchedule> findByTeamPlanner_IdAndTitleContainingOrderByStartTimeDesc(Long plannerId, String keyword);

    List<TeamSchedule> findByTeamPlanner_IdAndCreator_IdOrderByStartTimeDesc(Long plannerId, Long userId);

    @Query("SELECT e FROM TeamSchedule e " +
            "WHERE e.teamPlanner.id = :plannerId " +
            "AND e.creator.id <> :notCreatorId " +
            "AND :userId MEMBER OF e.memberIds")

    List<TeamSchedule> findByNotCreatorAndMemberIncluded(
            @Param("plannerId") Long plannerId,
            @Param("notCreatorId") Long notCreatorId,
            @Param("userId") Long userId
    );
}
