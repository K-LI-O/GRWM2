package GRWM.backend.service;

import GRWM.backend.dto.CategoryInfoDto;
import GRWM.backend.dto.PersonalScheduleCreateRequestDto;
import GRWM.backend.dto.PersonalScheduleDateTimeDto;
import GRWM.backend.dto.PersonalScheduleDto;
import GRWM.backend.entity.Member;
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


import java.util.Optional;

import static java.util.Optional.ofNullable;

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




}
