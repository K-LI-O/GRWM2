package GRWM.backend.repository.personalplanner;

import GRWM.backend.entity.user.Member;
import GRWM.backend.entity.personalplanner.PersonalPlanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalPlannerRepository extends JpaRepository<PersonalPlanner, Long> {


    List<PersonalPlanner> findByCreator(Member creator);
    // 개인 플래너 객체 저장
    // save(PersonalPlanner pp);

    // saveAll;
    //

}
