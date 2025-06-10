package GRWM.backend.controller;

import GRWM.backend.dto.PersonalPlannerCreateRequestDto;
import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.service.PersonalPlannerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/personal-planner")
public class PersonalPlannerController {

    private PersonalPlannerService ppService;

    @GetMapping
    public String createPlannerDemo(){
        return "ok";
    }

    @PostMapping("create")
    public String createPlanner(@RequestBody PersonalPlannerCreateRequestDto dto){

        ppService.createPersonalPlanner(dto);
        return "created!";
    }



}
