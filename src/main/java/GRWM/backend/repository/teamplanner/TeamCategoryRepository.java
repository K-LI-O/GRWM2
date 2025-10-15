package GRWM.backend.repository.teamplanner;

import GRWM.backend.entity.teamplanner.TeamCategory;
import GRWM.backend.entity.teamplanner.TeamPlanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamCategoryRepository extends JpaRepository<TeamCategory, Long> {

    // 카테고리 이름으로 해당 객체 존재 여부 확인
    Boolean existsByName(String name);

    // 플래너 객체로 카테고리 목록 찾기
    List<TeamCategory> findByTeamPlanner(TeamPlanner planner);

    //

}
