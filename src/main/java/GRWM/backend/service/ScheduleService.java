package GRWM.backend.service;

import GRWM.backend.dto.personalPlanner.*;
import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.entity.PlannerCategory;
import GRWM.backend.entity.Schedule;
import GRWM.backend.repository.MemberRepository;
import GRWM.backend.repository.PersonalPlannerRepository;
import GRWM.backend.repository.PlannerCategoryRepository;
import GRWM.backend.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final PersonalPlannerRepository ppRepository;
    private final PlannerCategoryRepository categoryRepository;

    /*
    함수명 : createPersonalSchedule
    기능 : 플래너의 스케줄을 생성하여 저장한다.
    매개변수 : PersonalScheduleCreateRequestDto
    반환값 : void
     */

    public Long createPersonalSchedule(PersonalScheduleCreateRequestDto dto) {

        // 플래너 가져오기
        System.out.println(dto.getPlannerId());
        PersonalPlanner planner = ppRepository.getReferenceById(dto.getPlannerId());
        // 플래너가 존재하지 않는 경우 예외 발생해야.

        // 카테고리 가져오기
        PlannerCategory category;
        if(dto.getCategoryId().isPresent())
        {
            category = categoryRepository.getReferenceById(dto.getCategoryId().get());
        } else {
            category = null;
        }



        Schedule schedule = new Schedule( // dto 기반으로 새로운 스케줄 생성
                dto.getTitle(), dto.getStartDateTime(), dto.getFinishDateTime(),
                dto.getLocation(), dto.getMemo(),
                planner, category);

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return savedSchedule.getId();

    }


        /*
    함수명 : showScheduleDetail
    기능 : 플래너의 특정 스케줄의 상세정보를 반환한다.
    매개변수 : Long scheduleId
    반환값 : dto
     */

    public PersonalScheduleDto showScheduleDetail(Long scheduleId){

        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule;
        if(optionalSchedule.isPresent()){
            schedule = optionalSchedule.get();
        } else {
            // 스케줄을 찾지 못했을 때의 로직
            throw new IllegalArgumentException("schedule not found with ID: " + scheduleId);
        }

        CategoryInfoDto categoryInfoDto;
        if(schedule.getPlannerCategory() == null){
            categoryInfoDto = null; // 카테고리를 선택하지 않을 시 0 반환;
        } else{
            categoryInfoDto = new CategoryInfoDto(
                    schedule.getPlannerCategory().getId(),
                    schedule.getPlannerCategory().getName(),
                    schedule.getPlannerCategory().getColor()
            );

        }

        PersonalScheduleDto dto = new PersonalScheduleDto(schedule.getId(), schedule.getTitle(), categoryInfoDto,
                schedule.getStartDateTime(), schedule.getFinishDateTime(), schedule.getLocation(), schedule.getMemo());

        return dto;
    }


    public void deleteSchedule(Long scheduleId){
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule;

        if(optionalSchedule.isPresent()){
            schedule = optionalSchedule.get();
            scheduleRepository.delete(schedule);

        } else {
            // 스케줄을 찾지 못했을 때의 로직
            throw new IllegalArgumentException("schedule not found with ID: " + scheduleId);
        }

    }



    /*
    함수명 : updateSchedule
    기능 : 플래너의 스케줄을 업데이트한다.
    매개변수 : Long scheduleId
    반환값 : void ;
     */


    @Transactional
    public void updateSchedule(Long scheduleId, PersonalScheduleDto dto){

        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule;

        if(optionalSchedule.isPresent()){
            schedule = optionalSchedule.get();
        } else {
            // 스케줄을 찾지 못했을 때의 로직
            throw new IllegalArgumentException("schedule not found with ID: " + scheduleId);
        }

        // 카테고리 Dto 를 일반 객체로 파싱
        PlannerCategory editedCategory;
        if(dto.getCategory() != null)
        {
            editedCategory = categoryRepository.getReferenceById(dto.getCategory().getCategoryId());
        } else {
            editedCategory = null;
        }

        schedule.setTitle(dto.getTitle());
        schedule.setPlannerCategory(editedCategory);
        schedule.setStartDateTime(dto.getStartDateTime());
        schedule.setFinishDateTime(dto.getFinishDateTime());
        schedule.setLocation(dto.getLocation());
        schedule.setMemo(dto.getMemo());
    }




    // 시간 업데이트
    /*
    함수명 : updateScheduleDateTime
    기능 : 플래너의 스케줄 시간만 업데이트한다.
    매개변수 : Long scheduleId, PersonalScheduleDateTimeDto dto
    반환값 : void ;
     */

    @Transactional
    public void updateScheduleDateTime(Long scheduleId, PersonalScheduleDateTimeDto dto){

        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule;

        if(optionalSchedule.isPresent()){
            schedule = optionalSchedule.get();
        } else {
            // 스케줄을 찾지 못했을 때의 로직
            throw new IllegalArgumentException("schedule not found with ID: " + scheduleId);
        }

        schedule.setStartDateTime(dto.getStartDateTime());
        schedule.setFinishDateTime(dto.getFinishDateTime());
    }



    // 먼슬리
    /* GET
    함수 이름 : showScheduleListMonthly
    기능 : 각 달마다의 일정 리스트를 보여준다.
    매개변수 : Long plannerId, int year, int month
    반환값 : List<dto>
     */

    public List<PersonalScheduleSimpleDto> showScheduleListMonthly(Long plannerId,
                                                                   int year,
                                                                   int month){
        // 날짜 형식으로 변환
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1); // 해당 월의 첫째 날 (예: 2025-06-01)
        LocalDate finishDate = yearMonth.atEndOfMonth();    // 해당 월의 마지막 날 (예: 2025-06-30)


        // 시작할 DateTime 구하기
        // 끝낼 DateTime 구하기
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 2025-06-01T00:00:00
        LocalDateTime finishDateTime = finishDate.atTime(23, 59, 59, 999999999); // 2025-06-30T23:59:59.999999999

        // 스케줄 객체 리스트 가져오기, 빈 dtoList 생성
        List<Schedule> monthlySchedule = scheduleRepository.findByPersonalPlannerIdAndStartDateTimeBetweenOrderByStartDateTimeAsc(plannerId, startDateTime, finishDateTime);
        List<PersonalScheduleSimpleDto> dtoList = new ArrayList<>();


        // dtoList 채우기
        for(Schedule schedule : monthlySchedule) {

            PersonalScheduleSimpleDto dto;
            if(schedule.getPlannerCategory() == null){
                dto = new PersonalScheduleSimpleDto(schedule.getId(), schedule.getTitle(),
                        null, null,
                        schedule.getStartDateTime(), schedule.getFinishDateTime()
                );

            } else{

                dto = new PersonalScheduleSimpleDto(schedule.getId(), schedule.getTitle(),
                        schedule.getPlannerCategory().getName(), schedule.getPlannerCategory().getColor(),
                        schedule.getStartDateTime(), schedule.getFinishDateTime()
                );
            }

            dtoList.add(dto);
        }

        return dtoList;
    }

    // 위클리
    /*
    함수 이름 : showScheduleListsWeekly
    기능 : 각 주마다의 일정 리스트를 보여준다.
    매개변수 : Long plannerId, int year, int weekNumber
    반환값 : List<dto>
     */

    public List<PersonalScheduleSimpleDto> showScheduleListWeekly(Long plannerId,
                                                                  int year, int weekOfYear) {

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

        List<Schedule> weeklySchedule = scheduleRepository.findByPersonalPlannerIdAndStartDateTimeBetweenOrderByStartDateTimeAsc(plannerId,
                startDateTime, finishDateTime);


        List<PersonalScheduleSimpleDto> dtoList = new ArrayList<>();


        // dtoList 채우기
        for(Schedule schedule : weeklySchedule) {

            PersonalScheduleSimpleDto dto;
            if(schedule.getPlannerCategory() == null){
                dto = new PersonalScheduleSimpleDto(schedule.getId(), schedule.getTitle(),
                        null, null,
                        schedule.getStartDateTime(), schedule.getFinishDateTime()
                );

            } else{

                dto = new PersonalScheduleSimpleDto(schedule.getId(), schedule.getTitle(),
                        schedule.getPlannerCategory().getName(), schedule.getPlannerCategory().getColor(),
                        schedule.getStartDateTime(), schedule.getFinishDateTime()
                );
            }

            dtoList.add(dto);
        }

        return dtoList;
    }


    // 데일리
    /* GET
    함수 이름 : showScheduleListDaily
    기능 : 각 하루마다의 일정 리스트를 보여준다.
    매개변수 : pathVariable int year, int month, int day
    반환값 : List<dto>
     */

    public List<PersonalScheduleSimpleDto> showScheduleListDaily(Long plannerId,
                                                                 int year, int month, int day){
        // 날짜 형식으로 변환
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(day);

        LocalDateTime startDateTime = startDate.atStartOfDay(); // 2025-06-01T00:00:00
        LocalDateTime finishDateTime = startDate.atTime(23, 59, 59, 999999999); // 2025-06-30T23:59:59.999999999


        // 스케줄 객체 리스트 가져오기, 빈 dtoList 생성
        List<Schedule> monthlySchedule = scheduleRepository.findByPersonalPlannerIdAndStartDateTimeBetweenOrderByStartDateTimeAsc(plannerId, startDateTime,finishDateTime);
        List<PersonalScheduleSimpleDto> dtoList = new ArrayList<>();


        // dtoList 채우기
        for(Schedule schedule : monthlySchedule) {

            PersonalScheduleSimpleDto dto;
            if(schedule.getPlannerCategory() == null){
                dto = new PersonalScheduleSimpleDto(schedule.getId(), schedule.getTitle(),
                        null, null,
                        schedule.getStartDateTime(), schedule.getFinishDateTime()
                );

            } else{

                dto = new PersonalScheduleSimpleDto(schedule.getId(), schedule.getTitle(),
                        schedule.getPlannerCategory().getName(), schedule.getPlannerCategory().getColor(),
                        schedule.getStartDateTime(), schedule.getFinishDateTime()
                );
            }

            dtoList.add(dto);
        }

        return dtoList;

    }


    /*
    함수명 : searchScheduleByKeyword
    기능 : 플래너 제목의 키워드로 플래너의 스케줄을 검색한다
    매개변수 : Long plannerId, String keyword
    반환값 : List<plannerScheduleSimpleDto>
     */

    public List<PersonalScheduleSimpleDto> searchScheduleByKeyword(Long plannerId, String keyword){

        // 해당하는 객체 리스트 가져오기
        List<Schedule> schedules = scheduleRepository.findByPersonalPlannerIdAndTitleContainingOrLocationContainingOrMemoContaining(
                plannerId, keyword, keyword, keyword);
        List<PersonalScheduleSimpleDto> dtoList = new ArrayList<>();

        // dto 리스트 채우기
        for(Schedule schedule : schedules) {

            PersonalScheduleSimpleDto dto;
            if(schedule.getPlannerCategory() == null){
                dto = new PersonalScheduleSimpleDto(schedule.getId(), schedule.getTitle(),
                        null, null,
                        schedule.getStartDateTime(), schedule.getFinishDateTime()
                );

            } else{

                dto = new PersonalScheduleSimpleDto(schedule.getId(), schedule.getTitle(),
                        schedule.getPlannerCategory().getName(), schedule.getPlannerCategory().getColor(),
                        schedule.getStartDateTime(), schedule.getFinishDateTime()
                );
            }

            dtoList.add(dto);
        }

        return dtoList;



    }




}
