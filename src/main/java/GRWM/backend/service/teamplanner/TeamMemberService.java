package GRWM.backend.service.teamplanner;

import GRWM.backend.dto.teamPlanner.TeamMemberBriefDto;
import GRWM.backend.dto.teamPlanner.TeamMemberDto;
import GRWM.backend.entity.teamplanner.TeamMember;
import GRWM.backend.entity.teamplanner.TeamPlanner;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.teamplanner.TeamMemberRepository;
import GRWM.backend.repository.teamplanner.TeamPlannerRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;
    private final TeamPlannerRepository teamPlannerRepository;


    /*
    함수명 : addMember
    기능 : 멤버 추가
    매개변수 : Long plannerId, Long MemberId,
    POST /api/team-planner/{plannerId}/member/{memberId}
    반환값: ResponseEntity<Void>
    */

    public void addMember(Long plannerId, Long memberId, String role){

        // 아이디로 사용자 불러오기
        Member member = extractOptionalMember(memberId);
        TeamPlanner planner = extractOptionalPlanner(plannerId);
        // 팀-멤버 객체 만들기

        TeamMember teamMember = TeamMember.builder()
                .member(member)
                .teamPlanner(planner)
                .role(role)
                .nickname("")
                .status("active")
                .build();


        // 객체 저장
        teamMemberRepository.save(teamMember);
    }


    /*
    함수명 : getMemberList
    기능 : 멤버 목록 가져오기(탈퇴 멤버는 제외)
    GET /api/team-planner/{plannerId}/member
    매개변수 : plannerId
    반환값 : List<MemberBriefDto> members
*/

    public List<TeamMemberDto> getTeamMemberList(Long plannerId){
        // 플래너 아이디로 플래너에 속한 팀-멤버 목록 불러오기
        List<TeamMember> members = teamMemberRepository.findByTeamPlanner(extractOptionalPlanner(plannerId));

        List<TeamMemberDto> dtoList = new ArrayList<>();
        for( TeamMember t : members){
            dtoList.add(memberToDto(t));
        }
        return dtoList;
    }



    /*
    함수명 : deleteMember
    기능 : 멤버 삭제(삭제 시 멤버는 남겨두고 status를 withdrawn변경)
    DELETE /api/team-planner/{plannerId}/member/{memberId}
    매개변수 : Long plannerId, Long plannerId
    반환값 : ResponseEntity 204
*/

    public void deleteMember(Long plannerId, Long memberId){
        // user 추출
        Member member = extractOptionalMember(memberId);
        TeamPlanner planner = extractOptionalPlanner(plannerId);
        // team member 추출
        TeamMember deletedMember = teamMemberRepository.findByTeamPlannerAndMember(planner, member);

        deletedMember.setStatus("withdrawn");
        teamMemberRepository.save(deletedMember);
    }


    /*
    함수명 : setMemberNickname
    기능 : 멤버 역할/별명 작성
    PUT /api/team-planner/{plannerId}/member/{memberId}/{roleName}
    매개변수 : Long plannerId, Long memberId, String roleName
    * 방장. 멤버 그 역할과는 별개.
*/

    public void setMemberNickName(Long plannerId, Long memberId, String nickname){
        TeamMember member = teamMemberRepository.findByTeamPlannerAndMember(
                extractOptionalPlanner(plannerId),
                extractOptionalMember(memberId)
        );

        member.setNickname(nickname);
        teamMemberRepository.save(member);
    }


    /*
    함수명 : addMemberToSchedule
    기능 : 일정에 참여하는 멤버 추가(일정 로직이긴 함)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/add-member
    매개변수 : Long plannerId, Long scheduleId
    반환값 : List<MemberBriefDto> members // 참여자 반환
     */



    /*
    함수명 : deleteMember
    기능 : 일정에 참여하는 멤버 삭제(일정 로직이긴 함)
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/delete-member
    매개변수 : Long plannerId, Long scheduleId
    반환값 : responseEntity 204
     */


    // ------- private 함수 ------- //

    private TeamMember extractOptionalTeamMember(Long memberId) throws RuntimeException{
        Optional<TeamMember> optionalTeamMember = teamMemberRepository.findById(memberId);

        if(optionalTeamMember.isPresent()){
            return optionalTeamMember.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 플래너 멤버입니다.");
        }
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


    private TeamPlanner extractOptionalPlanner(Long plannerId) throws RuntimeException{
        Optional<TeamPlanner> optionalPlanner = teamPlannerRepository.findById(plannerId);

        if(optionalPlanner.isPresent()){
            return optionalPlanner.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 플래너입니다.");
        }
    }

    public TeamMemberBriefDto memberToBriefDto(TeamMember member){
        TeamMemberBriefDto dto = TeamMemberBriefDto.builder()
                .userId(member.getMember().getId())
                .username(member.getMember().getUsername())
                .profileImage(member.getMember().getProfileImageLink())
                .status(member.getStatus())
                .build();
        return dto;
    }

    public TeamMemberDto memberToDto(TeamMember member){
        TeamMemberDto dto = TeamMemberDto.builder()
                .userId(member.getId())
                .username(member.getMember().getUsername())
                .nickname(member.getNickname())
                .profileImage(member.getMember().getProfileImageLink())
                .email(member.getMember().getEmail())
                .role(member.getRole())
                .status(member.getStatus())
                .build();

        return dto;
    }


}
