package GRWM.backend.controller.teamplanner;

import GRWM.backend.dto.teamPlanner.*;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.repository.teamplanner.TeamScheduleRepository;
import GRWM.backend.service.teamplanner.TeamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamScheduleController {

    private final TeamScheduleService teamScheduleService;

    /*
    name : createSchedule
    function : 일정 생성
    POST /api/team-planner/{plannerId}/schedule/create
    param : TeamPlannerCreateDto
    return value : Long scheduleId
    */
    @PostMapping("/api/team-planner/{plannerId}/schedule/create")
    public Long createSchedule(@PathVariable Long plannerId, @RequestBody TeamScheduleCreateDto dto,
                               @AuthenticationPrincipal CustomUserDetails userDetails){
        return teamScheduleService.createSchedule(dto, userDetails.getUserId());
    }


    /*
    name : getDetailSchedule
    function : 일정 상세보기
    GET /api/team-planner/{plannerId}/schedule/{scheduleId}
    param : Long plannerId, Long scheduleId
    return value : TeamScheduleDto
    */
    @GetMapping("/api/team-planner/{plannerId}/schedule/{scheduleId}")
    public TeamScheduleDto getDetailSchedule(@PathVariable Long plannerId, @PathVariable Long ScheduleId){
        return teamScheduleService.getDetailSchedule(plannerId, ScheduleId);
    }

    /*
    name : deleteSchedule
    function :스케줄 삭제하기
    DELETE /api/team-planner/{plannerId}/schedule/{scheduleId}/delete
    param : Long plannerId, Long scheduleId
    return value : x
    */
    @DeleteMapping("/api/team-planner/{plannerId}/schedule/{scheduleId}/delete")
    public void deleteSchedule(@PathVariable Long plannerId, @PathVariable Long scheduleId,
                               @AuthenticationPrincipal CustomUserDetails userDetails){
        teamScheduleService.deleteSchedule(plannerId, scheduleId, userDetails.getUserId());
    }

    /*
    name : updateSchedule
    function : 스케줄 수정하기
    PUT /api/team-planner/{plannerId}/schedule/{scheduleId}/edit
    param : Long plannerId, Long scheduleId,
    TeamScheduleUpdateDto
    return value : TeamScheduleDto
    */
    @PutMapping("/api/team-planner/{plannerId}/schedule/{scheduleId}/edit")
    public TeamScheduleDto updateSchedule(@PathVariable Long plannerId, @PathVariable Long scheduleId,
                                          @RequestBody TeamScheduleUpdateDto dto,
                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        return teamScheduleService.updateSchedule(plannerId, scheduleId, dto, userDetails.getUserId());
    }

    /*
    name : updateDateTimeSchedule
    function : 스케줄 DateTime만 수정하기(드래그앤드롭으로 스케줄 날짜 수정)
    PUT /api/team-lanner/{plannerId}/schedule/{scheduleId}/drag-drop
    param: Long plannerId, Long scheduleId
    TeamScheduleTimeUpdateDto
    response : x
    */

    @PutMapping("/api/team-lanner/{plannerId}/schedule/{scheduleId}/drag-drop")
    public void updateDateTimeSchedule(@PathVariable Long plannerId, @PathVariable Long scheduleId,
                                       @RequestBody TeamScheduleTimeUpdateDto dto){

        teamScheduleService.updateDateTimeSchedule(plannerId, scheduleId, dto);
    }

    /*
    name : getMonthlySchedules
    function : 먼슬리 일정 불러오기
    GET /api/team-planner/{plannerId}/schedule/monthly/{year}/{month}
    param : Long plannerId, int year, int month
    return value : List<TeamScheduleBriefDto> schedules
    */

    @GetMapping("/api/team-planner/{plannerId}/schedule/monthly/{year}/{month}")
    public List<TeamScheduleBriefDto> getMonthlySchedules(@PathVariable Long plannerId,
                                                          @PathVariable int year,
                                                          @PathVariable int month){
        return teamScheduleService.getMonthlySchedules(plannerId, year, month);
    }


    /*
    name : getWeeklySchedules
    function : 위클리 일정 불러오기
    GET /api/personal-planner/{plannerId}/schedule/weekly/{year}/{weekNumber}
    param : Long plannerId, int year, int weekNumber
    return value : List<TeamScheduleBriefDto> schedules
    */
    @GetMapping("/api/personal-planner/{plannerId}/schedule/weekly/{year}/{weekNumber}")
    public List<TeamScheduleBriefDto> getWeeklySchedules(@PathVariable Long plannerId,
                                                         @PathVariable int year,
                                                         @PathVariable int weekNumber){
        return teamScheduleService.getWeeklySchedules(plannerId, year, weekNumber);
    }

    /*
    name : getDailySchedules
    function : 데일리 일정 불러오기
    GET /api/personal-planner/{plannerId}/schedule/daily/{year}/{month}/{day}
    param : Long plannerId, int year, int month, int day
    return value : List<TeamScheduleBrieDto> schedules
     */
    @GetMapping("/api/personal-planner/{plannerId}/schedule/daily/{year}/{month}/{day}")
    public List<TeamScheduleBriefDto> getDailySchedules(@PathVariable Long plannerId,
                                                        @PathVariable int year,
                                                        @PathVariable int month,
                                                        @PathVariable int day){
        return teamScheduleService.getDailySchedules(plannerId, year, month, day);
    }




        /*
    함수명 : addMemberToSchedule
    기능 : 일정에 참여하는 멤버 추가(일정 로직이긴 함)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/add-member
    매개변수 : Long plannerId, Long scheduleId
    반환값 : List<MemberBriefDto> members
     */
    @PostMapping("/api/team-planner/{plannerId}/schedule/{scheduleId}/add-member")
    public List<TeamMemberBriefDto> addMemberToSchedule(@PathVariable Long plannerId, @PathVariable Long scheduleId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        return teamScheduleService.addMemberToSchedule(plannerId, scheduleId, userDetails.getUserId());

    }


    /*
    함수명 : deleteMember
    기능 : 일정에 참여하는 멤버 삭제(일정 로직이긴 함)
    DELETE /api/team-planner/{plannerId}/schedule/{scheduleId}/delete-member
    매개변수 : Long plannerId, Long scheduleId
    반환값 : responseEntity 204
     */
    @DeleteMapping("/api/team-planner/{plannerId}/schedule/{scheduleId}/delete-member")
    public void deleteMember(@PathVariable Long plannerId, @PathVariable Long scheduleId,
                             @AuthenticationPrincipal CustomUserDetails userDetails){
        teamScheduleService.deleteMember(plannerId, scheduleId, userDetails.getUserId());
    }


}
