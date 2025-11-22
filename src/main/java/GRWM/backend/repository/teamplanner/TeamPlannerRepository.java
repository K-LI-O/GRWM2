package GRWM.backend.repository.teamplanner;

import GRWM.backend.entity.teamplanner.TeamPlanner;
import GRWM.backend.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamPlannerRepository extends JpaRepository<TeamPlanner, Long> {
    List<TeamPlanner> findByCreator(Member member);

}
