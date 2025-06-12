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


    /*
    함수명 : showDetailSchedule
    기능 : 플래너의 스케줄의 내용을 보여준다
    매개변수 : Long scheduleId
    반환값 : PersonalScheduleDto
     */

    @GetMapping("/{scheduleId}")
    public PersonalScheduleDto showScheduleDetail(@PathVariable Long scheduleId){
        return scheduleService.showScheduleDetail(scheduleId);
    }


    /*
    함수명 : deleteSchedule
    기능 : 플래너의 스케줄을 삭제한다.
    매개변수 : Long scheduleId
    반환값 : ResponseEntity<Void> ;
     */

    @DeleteMapping("/{scheduleId}/delete")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId){

        try {
            scheduleService.deleteSchedule(scheduleId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // 자원을 찾을 수 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 기타 서버 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
    함수명 : editSchedule
    기능 : 플래너의 스케줄을 수정한다
    매개변수 : Long scheduleId, PersonalScheduleDto dto
    반환값 : planner
     */

    @PutMapping("/{scheduleId}/edit")
    public ResponseEntity<Void> editSchedule(@PathVariable Long scheduleId, @RequestBody PersonalScheduleDto dto){

        try {
            scheduleService.updateSchedule(scheduleId, dto);
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
