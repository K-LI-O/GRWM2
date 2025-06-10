package GRWM.backend.service;

import GRWM.backend.dto.PersonalPlannerCreateRequestDto;
import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.repository.PersonalPlannerRepository;
import org.apache.catalina.User;

import java.util.List;

public class PersonalPlannerService {

    PersonalPlannerRepository ppRepository;

    public void createPersonalPlanner(PersonalPlannerCreateRequestDto dto){


    }

    public List<PersonalPlanner> findPersonalPlannerList(User user){
        return ppRepository.findAll();
    }






}
