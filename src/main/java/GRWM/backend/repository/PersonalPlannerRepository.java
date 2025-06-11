package GRWM.backend.repository;

import GRWM.backend.entity.Member;
import GRWM.backend.entity.PersonalPlanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalPlannerRepository extends JpaRepository<PersonalPlanner, Long> {


    List<PersonalPlanner> findByCreator(Member creator);
    // 개인 플래너 객체 저장
    // save(PersonalPlanner pp);

    // saveAll;
    //

}
