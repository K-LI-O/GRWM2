package GRWM.backend.service;

import GRWM.backend.dto.PersonalPlannerCreateRequestDto;
import GRWM.backend.dto.PersonalPlannerListResponseDto;
import GRWM.backend.entity.Member;
import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.repository.MemberRepository;
import GRWM.backend.repository.PersonalPlannerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
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

    public List<PersonalPlannerListResponseDto> findPersonalPlannerList(Long memberId){

        Member searchMember = memberRepository.getReferenceById(memberId);
        List<PersonalPlanner> plannerList = ppRepository.findByCreator(searchMember);

        List<PersonalPlannerListResponseDto> dtoList = new ArrayList<>();

        for(PersonalPlanner planner : plannerList){
            PersonalPlannerListResponseDto dto = new PersonalPlannerListResponseDto(
                    planner.getCreator().getId(), planner.getName(), planner.getExplanation(), planner.getProfileImage()
            );

            dtoList.add(dto);
        }

        return dtoList;
    }






}
