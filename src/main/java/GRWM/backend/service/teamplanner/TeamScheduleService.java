package GRWM.backend.service.teamplanner;

import GRWM.backend.dto.teamPlanner.*;
import GRWM.backend.entity.personalplanner.Schedule;
import GRWM.backend.entity.teamplanner.TeamCategory;
import GRWM.backend.entity.teamplanner.TeamPlanner;
import GRWM.backend.entity.teamplanner.TeamSchedule;
import GRWM.backend.entity.teamplanner.TeamTodo;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.teamplanner.TeamCategoryRepository;
import GRWM.backend.repository.teamplanner.TeamMemberRepository;
import GRWM.backend.repository.teamplanner.TeamPlannerRepository;
import GRWM.backend.repository.teamplanner.TeamScheduleRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamScheduleService {

    private final TeamScheduleRepository scheduleRepository;
    private final TeamPlannerRepository teamPlannerRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamCategoryRepository teamCategoryRepository;


    /*
    name : createSchedule
    function : 일정 생성
    POST /api/team-planner/{plannerId}/schedule/create
    param : TeamPlannerCreateDto
    return value : Long scheduleId
    */
    @Transactional
    public Long createSchedule(TeamScheduleCreateDto dto, Long userId){
        // 일정 유효성 체크

        // 카테고리 아이디 체크
        TeamCategory category = null;
        if(dto.getCategoryId().isPresent()){
            category = teamCategoryRepository.getReferenceById(dto.getCategoryId().get());
        }
        TeamSchedule schedule = TeamSchedule.builder()
                .creator(memberRepository.getReferenceById(userId))
                .category(category)
                .title(dto.getTitle())
                .location(dto.getLocation())
                .memo(dto.getMemo())
                .editorRange(dto.getEditorRange())
                .startTime(dto.getStartDateTime())
                .finishTime(dto.getFinishDateTime())
                .teamPlanner(extractOptionalPlanner(dto.getPlannerId()))
                .build();
        // 일정 생성

        TeamSchedule savedSchedule = scheduleRepository.save(schedule);
        return savedSchedule.getId();
    }

    /*
    name : getDetailSchedule
    function : 일정 상세보기
    GET /api/team-planner/{plannerId}/schedule/{scheduleId}
    param : Long plannerId, Long scheduleId
    return value : TeamScheduleDto
    */
    public TeamScheduleDto getDetailSchedule(Long plannerId, Long scheduleId){
        // 일정 불러오기
        Optional<TeamSchedule> optionalSchedule = scheduleRepository.findById(scheduleId);

        if(optionalSchedule.isEmpty()){
            throw new RuntimeException("존재하지 않는 스케줄입니다.");
        }
        TeamSchedule schedule = optionalSchedule.get();
        Member creator = schedule.getCreator();

        // creator 와 category dto 생성
        if(teamPlannerRepository.findById(plannerId).isEmpty()){
            throw new RuntimeException("존재하지 않는 플래너입니다.");
        }
        TeamMemberBriefDto creatorDto = new TeamMemberBriefDto(creator.getId(), creator.getUsername(), creator.getProfileImageLink(), getStatus(teamPlannerRepository.getReferenceById(plannerId), creator));

        TeamCategoryDto categoryDto = null;
        if(schedule.getCategory() != null) {
            categoryDto.setCategoryId(schedule.getCategory().getId());
            categoryDto.setCategoryName(schedule.getCategory().getName());
            categoryDto.setColor(schedule.getCategory().getColor());
        }
        // dto에 담기
        TeamScheduleDto teamScheduleDto = TeamScheduleDto.builder()
                .creator(creatorDto)
                .title(schedule.getTitle())
                .category(categoryDto)
                .startDateTime(schedule.getStartTime())
                .finishDateTime(schedule.getFinishTime())
                .location(schedule.getLocation())
                .memo(schedule.getMemo())
                .editorRange(schedule.getEditorRange())
                .members(getMemberDtoList(teamPlannerRepository.getReferenceById(plannerId), schedule.getMemberIds()))
                .todoList(getTodoDtoList(teamPlannerRepository.getReferenceById(plannerId), schedule.getTodos()))
                .build();

        // 반환
        return teamScheduleDto;
    }

    /*
    name : deleteSchedule
    function :스케줄 삭제하기
    DELETE /api/team-planner/{plannerId}/schedule/{scheduleId}/delete
    param : Long plannerId, Long scheduleId
    return value : x
    */
    public void deleteSchedule(Long plannerId, Long scheduleId, Long userId){
        // 유효성 검사 및 객체 가져오기
        TeamSchedule schedule = extractOptionalSchedule(scheduleId);

        // 사용자 아이디와 스케줄 생성자 일치 확인
        if(!schedule.getCreator().getId().equals(userId)){
            throw new RuntimeException("스케줄 생성자만 삭제 가능합니다.");
        }
        Member member = extractOptionalMember(userId);

        // 스케줄 삭제
        scheduleRepository.delete(extractOptionalSchedule(scheduleId));
    }


    /*
    name : updateSchedule
    function : 스케줄 수정하기
    PUT /api/team-planner/{plannerId}/schedule/{scheduleId}/edit
    param : Long plannerId, Long scheduleId,
    TeamScheduleUpdateDto
    return value : TeamScheduleUpdateDto
    */

    public TeamScheduleDto updateSchedule(Long plannerId,
                                          Long scheduleId,
                                          TeamScheduleUpdateDto dto,
                                          Long userId){
          TeamSchedule schedule = extractOptionalSchedule(scheduleId);
        // 수정자 확인
        if(!schedule.getCreator().getId().equals(userId) && dto.getEditorRange().equals("Creator")){ // 생성자와 수정자가 일치한다면 일단 허용
            throw new RuntimeException("스케줄을 수정할 수 없습니다.");
        }

        // 맞는 수정자라면 수정
        /*
String title;
private LocalDateTime startDateTime,
LocalDateTime finishDateTime,
String location,
String memo,
String editorRange,
         */
        schedule.setTitle(dto.getTitle());
        schedule.setStartTime(dto.getStartDateTime());
        schedule.setFinishTime(dto.getFinishDateTime());
        schedule.setLocation(dto.getLocation());
        schedule.setMemo(dto.getMemo());
        schedule.setEditorRange(dto.getEditorRange());

        // 저장
        TeamSchedule savedSchedule = scheduleRepository.save(schedule);
        // DTO 반환;
        TeamScheduleDto result = TeamScheduleDto.builder()
                .creator(getCreatorDto(plannerId, savedSchedule))
                .title(savedSchedule.getTitle())
                .category(dto.getCategory())
                .startDateTime(savedSchedule.getStartTime())
                .finishDateTime(savedSchedule.getFinishTime())
                .location(savedSchedule.getLocation())
                .memo(savedSchedule.getMemo())
                .editorRange(savedSchedule.getEditorRange())
                .members(getMemberDtoList(teamPlannerRepository.getReferenceById(plannerId), savedSchedule.getMemberIds()))
                .todoList(getTodoDtoList(teamPlannerRepository.getReferenceById(plannerId), savedSchedule.getTodos()))
                .build();

        return result;
    }

    /*
    name : updateDateTimeSchedule
    function : 스케줄 DateTime만 수정하기(드래그앤드롭으로 스케줄 날짜 수정)
    PUT /api/team-lanner/{plannerId}/schedule/{scheduleId}/drag-drop
    param: Long plannerId, Long scheduleId
    TeamScheduleTimeUpdateDto
    response : x
    */

    public void updateDateTimeSchedule(Long plannerId, Long scheduleId, TeamScheduleTimeUpdateDto dto){
        // 스케줄 가져오기
        TeamSchedule schedule = extractOptionalSchedule(scheduleId);

        // 수정하기
        schedule.setStartTime(dto.getStartDateTime());
        schedule.setFinishTime(dto.getFinishDateTime());

        scheduleRepository.save(schedule);
    }



    /*
    name : addMember
    function : 일정에 참여하는 멤버 추가(개인이 버튼 누르기.)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/join/{userId}
    param : Long plannerId, Long scheduleId, Long userId
    return value : List<MemberBriefDto> members (실행 결과를 반영하여 일정에 참여하는 모든 멤버 반환)
    */
    public List<TeamMemberBriefDto> addMember(Long plannerId, Long scheduleId, Long userId){
        TeamSchedule schedule = extractOptionalSchedule(scheduleId);

        List<Long> members = schedule.getMemberIds();
        members.add(userId);

        schedule.setMemberIds(members);
        scheduleRepository.save(schedule);
        return getMemberDtoList(teamPlannerRepository.getReferenceById(plannerId), members);
    }

    /*
    name : getMonthlySchedules
    function : 먼슬리 일정 불러오기
    GET /api/team-planner/{plannerId}/schedule/monthly/{year}/{month}
    param : Long plannerId, int year, int month
    return value : List<TeamScheduleBriefDto> schedules
    */
    @Transactional(readOnly = true)
    public List<TeamScheduleBriefDto> getMonthlySchedules(Long plannerId, int year, int month){
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1); // 해당 월의 첫째 날 (예: 2025-06-01)
        LocalDate finishDate = yearMonth.atEndOfMonth();    // 해당 월의 마지막 날 (예: 2025-06-30)

        LocalDateTime startDateTime = startDate.atStartOfDay(); // 2025-06-01T00:00:00
        LocalDateTime finishDateTime = finishDate.atTime(23, 59, 59, 999999999); // 2025-06-30T23:59:59.999999999

        List<TeamSchedule> schedules = scheduleRepository.findByTeamPlannerIdAndStartTimeBetweenOrderByStartTimeAsc(
                plannerId,
                startDateTime,
                finishDateTime
        );

        List<TeamScheduleBriefDto> result = new ArrayList<>();
        for(TeamSchedule t : schedules){
            TeamScheduleBriefDto dto = TeamScheduleBriefDto.builder()
                    .scheduleId(t.getId())
                    .creator(getCreatorDto(plannerId, t))
                    .title(t.getTitle())
                    .category(getCategoryDto(t))
                    .startDateTime(t.getStartTime())
                    .finishDateTime(t.getFinishTime())
                    .location(t.getLocation())
                    .build();
            result.add(dto);
        }
        return result;
    }


    /*
    name : getWeeklySchedules
    function : 위클리 일정 불러오기
    GET /api/personal-planner/{plannerId}/schedule/weekly/{year}/{weekNumber}
    param : Long plannerId, int year, int weekNumber
    return value : List<TeamScheduleBriefDto> schedules
    */
    @Transactional(readOnly = true)
    public List<TeamScheduleBriefDto> getWeeklySchedules(Long plannerId, int year, int weekOfYear){
        // ISO 8601 WeekFields 인스턴스 생성 (월요일이 한 주의 시작, 최소 일수는 4일)
        WeekFields weekFields = WeekFields.ISO;

        // 특정 년도의 1월 1일로 시작
        LocalDate date = LocalDate.of(year, 1, 1);

        // 해당 주의 첫날(월요일) 찾기
        LocalDate firstDayOfTargetWeek = date.with(weekFields.weekOfYear(), weekOfYear)
                .with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));


        // 해당 주의 마지막날(일요일) 찾기
        LocalDate lastDayOfTargetWeek = firstDayOfTargetWeek.with(TemporalAdjusters.nextOrSame(weekFields.getFirstDayOfWeek().plus(6)));

        // LocalDateTime 으로 변환 (날짜 범위 시작은 자정, 종료는 그 날의 마지막 밀리초)
        LocalDateTime startDateTime = firstDayOfTargetWeek.atStartOfDay(); // 해당 주의 월요일 00:00:00
        LocalDateTime finishDateTime = lastDayOfTargetWeek.atTime(23, 59, 59, 999999999); // 해당 주의 일요일 23:59:59.999999999

        List<TeamSchedule> weeklySchedules = scheduleRepository.findByTeamPlannerIdAndStartTimeBetweenOrderByStartTimeAsc(

                plannerId, startDateTime,finishDateTime
        );
        List<TeamScheduleBriefDto> result = new ArrayList<>();


        for(TeamSchedule t : weeklySchedules){

            TeamScheduleBriefDto dto = TeamScheduleBriefDto.builder()
                    .scheduleId(t.getId())
                    .creator(getCreatorDto(plannerId, t))
                    .title(t.getTitle())
                    .category(getCategoryDto(t))
                    .startDateTime(t.getStartTime())
                    .finishDateTime(t.getFinishTime())
                    .location(t.getLocation())
                    .build();
            result.add(dto);
        }
        return result;
    }


    /*
    name : getDailySchedules
    function : 데일리 일정 불러오기
    GET /api/personal-planner/{plannerId}/schedule/daily/{year}/{month}/{day}
    param : Long plannerId, int year, int month, int day
    return value : List<TeamScheduleBrieDto> schedules
     */
    @Transactional(readOnly = true)
    public List<TeamScheduleBriefDto> getDailySchedules(Long plannerId, int year, int month, int day){
        // 날짜 형식으로 변환
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(day);

        LocalDateTime startDateTime = startDate.atStartOfDay(); // 2025-06-01T00:00:00
        LocalDateTime finishDateTime = startDate.atTime(23, 59, 59, 999999999); // 2025-06-30T23:59:59.999999999
        System.out.println(startDateTime);
        System.out.println(finishDateTime);
        System.out.println(extractOptionalPlanner(plannerId).getId());
        List<TeamSchedule> dailySchedules = scheduleRepository.findByTeamPlannerIdAndStartTimeBetweenOrderByStartTimeAsc(
                plannerId, startDateTime, finishDateTime
        );
        System.out.println(dailySchedules.size());
        List<TeamScheduleBriefDto> result = new ArrayList<>();

        for(TeamSchedule t : dailySchedules){
            TeamScheduleBriefDto dto = TeamScheduleBriefDto.builder()
                    .scheduleId(t.getId())
                    .creator(getCreatorDto(plannerId, t))
                    .title(t.getTitle())
                    .category(getCategoryDto(t))
                    .startDateTime(t.getStartTime())
                    .finishDateTime(t.getFinishTime())
                    .location(t.getLocation())
                    .build();
            result.add(dto);
        }
        return result;
    }



        /*
    함수명 : addMemberToSchedule
    기능 : 일정에 참여하는 멤버 추가(일정 로직이긴 함)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/add-member
    매개변수 : Long plannerId, Long scheduleId
    반환값 : List<MemberBriefDto> members
     */
        public List<TeamMemberBriefDto> addMemberToSchedule(Long plannerId, Long scheduleId, Long userId){
            TeamSchedule schedule = extractOptionalSchedule(scheduleId);

            List<Long> members = schedule.getMemberIds();

            // 이미 참여하는 멤버가 아닐 때에만 로직 실행
            if(!members.contains(userId)){
                members.add(userId);
                schedule.setMemberIds(members);
                scheduleRepository.save(schedule);
            }
            return getMemberDtoList(teamPlannerRepository.getReferenceById(plannerId), members);
        }


    /*
    함수명 : deleteMember
    기능 : 일정에 참여하는 멤버 삭제(일정 로직이긴 함)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/delete-member
    매개변수 : Long plannerId, Long scheduleId
    반환값 : responseEntity 204
     */
    public void deleteMember(Long plannerId, Long scheduleId, Long userId){
        TeamSchedule schedule = extractOptionalSchedule(scheduleId);

        List<Long> members = schedule.getMemberIds();
        if(members.contains(userId)) {
            members.remove(userId);
        }
        scheduleRepository.save(schedule);
    }


    // ======= private logics ======= //

    private String getStatus(TeamPlanner planner, Member member){

        return teamMemberRepository.findByTeamPlannerAndMember(planner, member).getStatus();
    }

    private List<TeamMemberBriefDto> getMemberDtoList(TeamPlanner planner, List<Long> memberIds){
        if(memberIds.isEmpty()){
            return null;
        }

        List<Member> members = new ArrayList<>();
        for(Long t: memberIds){
            members.add((memberRepository.findById(t).isPresent()) ?  memberRepository.getReferenceById(t): null);
        }

        List<TeamMemberBriefDto> result = new ArrayList<>();

         for(Member t : members){
             TeamMemberBriefDto dto = TeamMemberBriefDto.builder()
                     .userId(t.getId())
                     .username(t.getUsername())
                     .profileImage(t.getProfileImageLink())
                     .status(getStatus(planner, t))
                     .build();
             result.add(dto);
         }
         return result;
    }

    private TeamMemberBriefDto getMemberDto(TeamPlanner planner, Member t){

            TeamMemberBriefDto dto = TeamMemberBriefDto.builder()
                    .userId(t.getId())
                    .username(t.getUsername())
                    .profileImage(t.getProfileImageLink())
                    .status(getStatus(planner, t))
                    .build();

        return dto;
    }

    private List<TeamTodoDto> getTodoDtoList(TeamPlanner planner, List<TeamTodo> todos){
        List<TeamTodoDto> result = new ArrayList<>();
        for(TeamTodo t : todos){
            // userDto 추출;
            TeamMemberBriefDto memberDto = getMemberDto(planner, t.getCreator());
            TeamTodoDto dto = TeamTodoDto.builder()
                    .todoId(t.getId())
                    .user(memberDto)
                    .content(t.getContent())
                    .isCompleted(t.isCompleted())
                    .isPrivate(t.isPrivate())
                    .build();
            result.add(dto);
        }
        return result;
    }

    private Member extractOptionalMember(Long userId) throws RuntimeException{
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if(optionalMember.isPresent()){
            return optionalMember.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }

    private TeamMemberBriefDto getCreatorDto(Long plannerId, TeamSchedule t){
        TeamMemberBriefDto creatorDto = new TeamMemberBriefDto(
                t.getCreator().getId(),
                t.getCreator().getUsername(),
                t.getCreator().getProfileImageLink(),
                getStatus(teamPlannerRepository.getReferenceById(plannerId), t.getCreator())
        );
        return creatorDto;
    }

    private TeamCategoryDto getCategoryDto(TeamSchedule t){

        if(t.getCategory() == null) return null;
        TeamCategory cat = teamCategoryRepository.getReferenceById(t.getCategory().getId());
        TeamCategoryDto categoryDto = TeamCategoryDto.builder()
                .categoryId(cat.getId())
                .categoryName(cat.getName())
                .color(cat.getColor())
                .build();
        return categoryDto;
    }

    private TeamSchedule extractOptionalSchedule(Long scheduleId) {
        Optional<TeamSchedule> optionalSchedule = scheduleRepository.findById(scheduleId);

        if(optionalSchedule.isEmpty()){
            throw new RuntimeException("존재하지 않는 스케줄입니다.");
        }
        return optionalSchedule.get();
    }

    private TeamPlanner extractOptionalPlanner(Long plannerId) throws RuntimeException{
        Optional<TeamPlanner> optionalPlanner = teamPlannerRepository.findById(plannerId);

        if(optionalPlanner.isPresent()){
            return optionalPlanner.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 공유플래너입니다.");
        }
    }

}
