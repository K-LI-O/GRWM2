package GRWM.backend.repository.personalplanner;

import GRWM.backend.entity.personalplanner.PersonalPlanner;
import GRWM.backend.entity.personalplanner.PlannerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerCategoryRepository extends JpaRepository<PlannerCategory, Long> {


    List<PlannerCategory> findByPersonalPlanner(PersonalPlanner personalPlanner);
}
