package GRWM.backend.service;

import GRWM.backend.dto.PersonalScheduleCreateRequestDto;
import GRWM.backend.dto.PersonalScheduleDto;
import GRWM.backend.entity.Member;
import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.entity.PlannerCategory;
import GRWM.backend.entity.Schedule;
import GRWM.backend.repository.MemberRepository;
import GRWM.backend.repository.PersonalPlannerRepository;
import GRWM.backend.repository.PlannerCategoryRepository;
import GRWM.backend.repository.ScheduleRepository;
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
            // 플래너를 찾지 못했을 때의 로직
            throw new IllegalArgumentException("schedule not found with ID: " + scheduleId);
        }

        Long categoryId;
        if(schedule.getPlannerCategory() == null){
            categoryId = 0L; // 카테고리를 선택하지 않을 시 0 반환;
        } else{
            categoryId = schedule.getPlannerCategory().getId();
        }

        PersonalScheduleDto dto = new PersonalScheduleDto(schedule.getId(), categoryId, schedule.getTitle(),
                schedule.getStartDateTime(), schedule.getFinishDateTime(), schedule.getLocation(), schedule.getMemo());

        return dto;
    }








}
