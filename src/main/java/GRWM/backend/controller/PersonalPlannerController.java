package GRWM.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu/personal-planner")
public class PersonalPlannerController {

    @GetMapping
    public String demoController(){
        return "ok";
    }


}
