package GRWM.backend.repository;

import GRWM.backend.entity.PersonalPlanner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalPlannerRepository extends JpaRepository<PersonalPlanner, Long> {


    // 개인 플래너 객체 저장
    // save(PersonalPlanner pp);

    // saveAll;
    //

}
