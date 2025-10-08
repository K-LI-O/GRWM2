package GRWM.backend.repository.teamplanner;

import GRWM.backend.entity.teamplanner.TeamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamScheduleRepository extends JpaRepository<TeamSchedule, Long> {
}
