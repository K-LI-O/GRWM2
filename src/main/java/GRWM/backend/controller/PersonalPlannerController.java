package GRWM.backend.controller;

import GRWM.backend.dto.PersonalPlannerCreateRequestDto;
import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.service.PersonalPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/personal-planner")
public class PersonalPlannerController {

    private final PersonalPlannerService ppService;


    /*
    함수명 : createPlanner
    기능 : 플래너 생성 dto 를 받아 서비스로 넘기고 만들어진 플래너 아이디를 반환한다.
    매개변수 : Long creatorId, String title, String explanation, String profileImageLink
    반환값 : ResponseEntity<Long>; Long 타입 플래너 아이디 반환;
     */

    @PostMapping("create")
    public ResponseEntity<Long> createPlanner(@RequestBody PersonalPlannerCreateRequestDto dto){

        Long savedPlannerId = ppService.createPersonalPlanner(dto);
        return ResponseEntity.ok(savedPlannerId);
    }



}
