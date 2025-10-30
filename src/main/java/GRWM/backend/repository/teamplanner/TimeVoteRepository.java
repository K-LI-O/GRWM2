package GRWM.backend.repository.teamplanner;

import GRWM.backend.entity.teamplanner.TeamPlanner;
import GRWM.backend.entity.teamplanner.TimeVote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeVoteRepository extends JpaRepository<TimeVote, Long> {
    public List<TimeVote> findByTeamPlanner(TeamPlanner planner, Pageable pageable);
}
