package GRWM.backend.controller;


import GRWM.backend.dto.PersonalScheduleCreateRequestDto;
import GRWM.backend.dto.PersonalScheduleDto;
import GRWM.backend.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/personal-planner/schedule")
public class PersonalScheduleController {

    private final ScheduleService scheduleService;


        /*
    함수명 : createSchedule
    기능 : 플래너의 스케줄을 생성하여 저장한다.
    매개변수 : PersonalScheduleCreateRequestDto
    반환값 : ResponseEntity<Long> ; 저장한 스케줄 아이디를 반환한다.
     */

    @PostMapping("/create")
    public ResponseEntity<Long> createSchedule(@RequestBody PersonalScheduleCreateRequestDto dto){

        Long scheduleID = scheduleService.createPersonalSchedule(dto);
        return ResponseEntity.ok(scheduleID);
    }

    @GetMapping("/{scheduleId}")
    public PersonalScheduleDto showScheduleDetail(@PathVariable Long scheduleId){
        return scheduleService.showScheduleDetail(scheduleId);
    }


}
