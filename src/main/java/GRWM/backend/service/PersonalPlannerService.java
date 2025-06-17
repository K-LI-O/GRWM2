package GRWM.backend.service;

import GRWM.backend.dto.personalPlanner.PersonalPlannerCreateRequestDto;
import GRWM.backend.dto.personalPlanner.PersonalPlannerDto;
import GRWM.backend.dto.personalPlanner.PersonalPlannerListResponseDto;
import GRWM.backend.entity.Member;
import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.repository.MemberRepository;
import GRWM.backend.repository.PersonalPlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonalPlannerService {

    private final PersonalPlannerRepository ppRepository;
    private final MemberRepository memberRepository;


     /*
    함수명 : createPersonalPlanner
    기능 : 플래너 생성 dto 를 기반으로 플래너 객체를 생성하여 저장한다.
    매개변수 : Long creatorId, String title, String explanation, String profileImageLink
    반환값 : Long; 플래너 아이디 반환
     */

    public Long createPersonalPlanner(PersonalPlannerCreateRequestDto dto){

        Member creator = memberRepository.getReferenceById(dto.getCreatorId());
        PersonalPlanner pp = new PersonalPlanner(creator, dto.getTitle(), dto.getExplanation(), dto.getProfileImageLink());

        PersonalPlanner savedPlanner = ppRepository.save(pp);

        return savedPlanner.getId();
    }



    /*
    함수명 : findPersonalPlannerList
    기능 : memberId로 생성한 플래너 리스트를 조회한다.
    매개변수 : Long memberId
    반환값 : List<PersonalPlannerListResponseDto> 플래너 dto list 반환
     */

    @Transactional(readOnly = true)
    public List<PersonalPlannerDto> findPersonalPlannerList(Long memberId){

        Member searchMember = memberRepository.getReferenceById(memberId);
        List<PersonalPlanner> plannerList = ppRepository.findByCreator(searchMember);

        List<PersonalPlannerDto> dtoList = new ArrayList<>();

        for(PersonalPlanner planner : plannerList){
            PersonalPlannerDto dto = new PersonalPlannerDto(
                    planner.getId(), planner.getName(), planner.getExplanation(), planner.getProfileImage()
            );

            dtoList.add(dto);
        }

        return dtoList;
    }


    @Transactional(readOnly = true)
    public PersonalPlannerListResponseDto editPersonalPlanner(Long plannerId){
        Optional<PersonalPlanner> optionalPlanner =  ppRepository.findById(plannerId);

        if (optionalPlanner.isPresent()) {

            PersonalPlanner planner = optionalPlanner.get(); // 플래너 객체 가져오기

            return new PersonalPlannerListResponseDto(
                    planner.getCreator().getId(), planner.getName(), planner.getExplanation(), planner.getProfileImage()
            );

        } else {
            // 플래너를 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Planner not found with ID: " + plannerId);
        }
    }

    public PersonalPlannerDto upDatePersonalPlanner(PersonalPlannerDto dto){

        Optional<PersonalPlanner> optionalPlanner = ppRepository.findById(dto.getPlannerId());
        PersonalPlanner planner;

        if(optionalPlanner.isPresent()){
            planner = optionalPlanner.get();
            planner.setName(dto.getTitle());
            planner.setExplanation(dto.getExplanation());
            planner.setProfileImage(dto.getProfileImageLink());
        }
        else {
            // 플래너를 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Planner not found with ID: " + dto.getPlannerId());
        }
        PersonalPlanner savedPlanner = ppRepository.save(planner);

        PersonalPlannerDto savedDto = new PersonalPlannerDto(
                savedPlanner.getId(), savedPlanner.getName(), savedPlanner.getExplanation(), savedPlanner.getProfileImage());

        return savedDto;
    }


    public void deletePersonalPlanner(Long plannerId){

        Optional<PersonalPlanner> optionalPlanner = ppRepository.findById(plannerId);
        PersonalPlanner planner;

        if(optionalPlanner.isPresent()){
            planner = optionalPlanner.get();
            ppRepository.delete(planner);
        }
        else {
            // 플래너를 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Planner not found with ID: " + plannerId);
        }


    }






}
