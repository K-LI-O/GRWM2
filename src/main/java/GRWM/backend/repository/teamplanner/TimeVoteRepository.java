package GRWM.backend.repository.teamplanner;

import GRWM.backend.entity.teamplanner.TimeVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeVoteRepository extends JpaRepository<TimeVote, Long> {
}
