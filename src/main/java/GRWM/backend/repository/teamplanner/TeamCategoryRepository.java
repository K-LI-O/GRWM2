package GRWM.backend.repository.teamplanner;

import GRWM.backend.entity.teamplanner.TeamCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamCategoryRepository extends JpaRepository<TeamCategory, Long> {
}
