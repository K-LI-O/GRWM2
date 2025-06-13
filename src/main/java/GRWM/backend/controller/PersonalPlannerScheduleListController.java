package GRWM.backend.controller;


import GRWM.backend.dto.personalPlanner.PersonalScheduleSimpleDto;
import GRWM.backend.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/personal-planner/{plannerId}")
@RequiredArgsConstructor
public class PersonalPlannerScheduleListController {

    private final ScheduleService scheduleService;

    // 먼슬리
    /* GET
    함수 이름 : showScheduleListsMonthly
    기능 : 각 달마다의 일정 리스트를 보여준다.
    매개변수 : pathVariable int year, int month
    반환값 : List<dto>
     */
    @GetMapping("/{year}/{month}/monthly")
    public List<PersonalScheduleSimpleDto> showScheduleListMonthly(@PathVariable Long plannerId,
                                                                   @PathVariable int year,
                                                                   @PathVariable int month){


        return scheduleService.showScheduleListMonthly(plannerId, year, month);
    }



    // 위클리
    /* GET
    함수 이름 : showScheduleListsWeekly
    기능 : 각 주마다의 일정 리스트를 보여준다.
    매개변수 : pathVariable int year, int weekNumber
    반환값 : List<dto>
     */

    @GetMapping("/{year}/{weekNumber}/weekly")
    public List<PersonalScheduleSimpleDto> showScheduleListWeekly(@PathVariable Long plannerId,
                                                                  @PathVariable int year,
                                                                  @PathVariable int weekNumber) {

        return scheduleService.showScheduleListWeekly(plannerId,year, weekNumber);
    }



    // 데일리
    /* GET
    함수 이름 : showScheduleListsDaily
    기능 : 각 하루마다의 일정 리스트를 보여준다.
    매개변수 : pathVariable int year, int month, int day
    반환값 : List<dto>
     */

    @GetMapping("/{year}/{month}/{day}/daily")
    public List<PersonalScheduleSimpleDto> showScheduleLIstDaily(@PathVariable Long plannerId,
                                                                 @PathVariable int year,
                                                                 @PathVariable int month,
                                                                 @PathVariable int day){

        return scheduleService.showScheduleListDaily(plannerId, year, month, day);

    }



        /*
    함수명 : searchScheduleByKeyword
    기능 : 플래너 제목의 키워드로 플래너의 스케줄을 검색한다
    매개변수 : Long plannerId, String keyword
    반환값 : List<plannerScheduleSimpleDto>
     */

    @GetMapping("/search/{keyword}")
    public List<PersonalScheduleSimpleDto> searchScheduleByKeyword(@PathVariable Long plannerId,
                                                                   @PathVariable String keyword) {

        return scheduleService.searchScheduleByKeyword(plannerId, keyword);

    }


}
