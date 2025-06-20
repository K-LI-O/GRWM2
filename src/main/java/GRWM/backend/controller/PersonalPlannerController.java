package GRWM.backend.controller;

import GRWM.backend.dto.personalPlanner.PersonalPlannerCreateRequestDto;
import GRWM.backend.dto.personalPlanner.PersonalPlannerDto;
import GRWM.backend.dto.personalPlanner.PersonalPlannerListResponseDto;
import GRWM.backend.service.PersonalPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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


    /*
    함수명 : showPlannerList
    기능 : userId로 생성한 플래너 리스트를 조회한다.
    매개변수 : Long userId
    반환값 : List<PersonalPlannerListResponseDto> 플래너 dto list 반환
     */

    @GetMapping("list/{userId}")
    public List<PersonalPlannerDto> showPlannerList(@PathVariable Long userId){ // userId
        List<PersonalPlannerDto> dto = ppService.findPersonalPlannerList(userId);

        return dto;
    }


        /*
    함수명 : editPlanner
    기능 : plannerId로 생성한 플래너의 elements를 불러온다
    매개변수 : Long plannerId
    반환값 : Long plannerId, String title, String explanation, String profileImageLink
     */

    @GetMapping("list/{plannerId}/edit")
    public PersonalPlannerListResponseDto editPlanner(@PathVariable Long plannerId){
        PersonalPlannerListResponseDto dto = ppService.editPersonalPlanner(plannerId);

        return dto;
    }

            /*
    함수명 : updatePlanner
    기능 : 플래너의 elements 를 업데이트한다.
    매개변수 : Long plannerId, String title, String explanation, String profileImageLink
    반환값 : Long plannerId, String title, String explanation, String profileImageLink
     */

    @PutMapping("list/{plannerId}/edit")
    public PersonalPlannerDto updatePlanner(@PathVariable Long plannerId, @RequestBody PersonalPlannerDto dto){

        return ppService.upDatePersonalPlanner(dto);
    }


    /*
    함수명 : deletePlanner
    기능 : 플래너를 삭제한다. 킬킬
    매개변수 : Long plannerId
    반환값 : ResponseEntity<Void>
     */

    @DeleteMapping("/list/{plannerId}/delete")
    public ResponseEntity<Void> updatePlanner(@PathVariable Long plannerId){

        try {
            ppService.deletePersonalPlanner(plannerId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
        // 자원을 찾을 수 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
        // 기타 서버 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().build();
        }

    }




}
