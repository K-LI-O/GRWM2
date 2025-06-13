package GRWM.backend.repository;

import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.entity.PlannerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerCategoryRepository extends JpaRepository<PlannerCategory, Long> {


    List<PlannerCategory> findByPersonalPlanner(PersonalPlanner personalPlanner);
}
