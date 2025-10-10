package GRWM.backend.service.teamplanner;

import GRWM.backend.dto.teamPlanner.TeamMemberBriefDto;
import GRWM.backend.dto.teamPlanner.TeamPlannerCreateDto;
import GRWM.backend.dto.teamPlanner.TeamPlannerDto;
import GRWM.backend.dto.teamPlanner.TeamPlannerUpdateDto;
import GRWM.backend.entity.teamplanner.TeamMember;
import GRWM.backend.entity.teamplanner.TeamPlanner;

import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.teamplanner.TeamPlannerRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamPlannerService {

    private final TeamPlannerRepository teamPlannerRepository;
    private final MemberRepository memberRepository;


        /*
    name : createPlanner
    function : 플래너 생성
    POST /api/team-planner/create
    param : TeamPlannerCreateDto
{
Long creatorId,
String title,
String description,
String profileImage
}
    return value : ResponseEntity<Long> plannerId 반환;
    */

    public Long createPlanner(TeamPlannerCreateDto dto, Long userId){

        // 사용자 객체 불러오기

        TeamPlanner planner = TeamPlanner.builder()
                .creator(extractOptionalMember(userId))
                .title(dto.getTitle())
                .description(dto.getDescription())
                .profileImageLink(dto.getProfileImage())
                .build();

        planner = teamPlannerRepository.save(planner);
        return planner.getId();
    }


    /*
    name : getPlannerList
    function : 플래너 목록 조회
    GET /api/team-planner/list
    param : x
    return value : List<TeamPlannerDto>
    */

    public List<TeamPlannerDto> getPlannerList(Long userId){
        List<TeamPlanner> planners = teamPlannerRepository.findByCreator(extractOptionalMember(userId));

        List<TeamPlannerDto> dtoList = new ArrayList<>();

        for(TeamPlanner t : planners){
            dtoList.add(plannerToDto(t));
        }
        return dtoList;
    }

    /*
    name : updatePlanner
    function : 플래너 업데이트
    PUT /api/team-planner/{plannerId}/update
    param : Long plannerId
    return value : TeamPlannerDto
    */


    public TeamPlannerDto updatePlanner(Long plannerId, TeamPlannerUpdateDto dto){
        TeamPlanner planner = extractOptionalPlanner(plannerId);

        planner.setTitle(dto.getTitle());
        planner.setProfileImageLink(dto.getProfileImage());
        planner.setDescription(dto.getDescription());

        planner = teamPlannerRepository.save(planner);
        return plannerToDto(planner);
    }


    /*
    name : deletePlanner
    function : 플래너 삭제
    DELETE /api/personal-planner/{plannerId}/delete
    param : Long plannerId
    return value : x
     */

    public void deletePlanner(Long plannerId){
        teamPlannerRepository.delete(extractOptionalPlanner(plannerId));
    }





    //======= private logic ======= //

    private Member extractOptionalMember(Long userId) throws RuntimeException{
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if(optionalMember.isPresent()){
            return optionalMember.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
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


    private TeamPlannerDto plannerToDto(TeamPlanner planner){
        List<TeamMember> members = planner.getTeamMembers();
        List<TeamMemberBriefDto> dtoList = new ArrayList<>();
        for(TeamMember t : members){
            dtoList.add(memberToBriefDto(t));
        }

        TeamPlannerDto dto = TeamPlannerDto.builder()
                .plannerId(planner.getId())
                .title(planner.getTitle())
                .description(planner.getDescription())
                .profileImageLink(planner.getProfileImageLink())
                .members(dtoList)
                .build();

        return dto;
    }



    private TeamMemberBriefDto memberToBriefDto(TeamMember member){
        TeamMemberBriefDto dto = TeamMemberBriefDto.builder()
                .userId(member.getMember().getId())
                .username(member.getMember().getUsername())
                .profileImage(member.getMember().getProfileImageLink())
                .status(member.getStatus())
                .build();
        return dto;
    }

}
