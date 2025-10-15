package GRWM.backend.service.teamplanner;

import GRWM.backend.repository.teamplanner.TeamScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamScheduleService {

    private final TeamScheduleRepository scheduleRepository;


        /*
    name : createSchedule
    function : 일정 생성
    POST /api/team-planner/{plannerId}/schedule/create
    param : TeamPlannerCreateDto
    return value : Long scheduleId
    */

    /*
    name : getDetailSchedule
    function : 일정 상세보기
    GET /api/team-planner/{plannerId}/schedule/{scheduleId}
    param : Long plannerId, Long scheduleId
    return value : TeamScheduleDto
    */

    /*
    name : deleteSchedule
    function :스케줄 삭제하기
    DELETE /api/team-planner/{plannerId}/schedule/{scheduleId}/delete
    param : Long plannerId, Long scheduleId
    return value : x
    */

    /*
    name : updateSchedule
    function : 스케줄 수정하기
    PUT /api/team-planner/{plannerId}/schedule/{scheduleId}/edit
    param : Long plannerId, Long scheduleId,
    TeamScheduleUpdateDto
    return value : TeamScheduleDto
    */

    /*
    name : updateDateTimeSchedule
    function : 스케줄 DateTime만 수정하기(드래그앤드롭으로 스케줄 날짜 수정)
    PUT /api/team-lanner/{plannerId}/schedule/{scheduleId}/drag-drop
    param: Long plannerId, Long scheduleId
    TeamScheduleTimeUpdateDto
    response : x
    */

    /*
    name : addMember
    function : 일정에 참여하는 멤버 추가(개인이 버튼 누르기.)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/join/{userId}
    param : Long plannerId, Long scheduleId, Long userId
    return value : List<MemberBriefDto> members (실행 결과를 반영하여 일정에 참여하는 모든 멤버 반환)
    */

    /*
    name : getMonthlySchedules
    function : 먼슬리 일정 불러오기
    GET /api/team-planner/{plannerId}/schedule/monthly/{year}/{month}
    param : Long plannerId, int year, int month
    return value : List<TeamScheduleBriefDto> schedules
    */


    /*
    name : getWeeklySchedules
    function : 위클리 일정 불러오기
    GET /api/personal-planner/{plannerId}/schedule/weekly/{year}/{weekNumber}
    param : Long plannerId, int year, int weekNumber
    return value : List<TeamScheduleBriefDto> schedules
    */


    /*
    name : getDailySchedules
    function : 데일리 일정 불러오기
    GET /api/personal-planner/{plannerId}/schedule/daily/{year}/{month}/{day}
    param : Long plannerId, int year, int month, int day
    return value : List<TeamScheduleBrieDto> schedules
     */









        /*
    함수명 : addMemberToSchedule
    기능 : 일정에 참여하는 멤버 추가(일정 로직이긴 함)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/add-member
    매개변수 : Long plannerId, Long scheduleId
    반환값 : List<MemberBriefDto> members
     */


    /*
    함수명 : deleteMember
    기능 : 일정에 참여하는 멤버 삭제(일정 로직이긴 함)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/delete-member
    매개변수 : Long plannerId, Long scheduleId
    반환값 : responseEntity 204
     */

}
