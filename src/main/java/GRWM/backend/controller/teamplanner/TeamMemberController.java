package GRWM.backend.controller.teamplanner;

import GRWM.backend.dto.teamPlanner.TeamMemberBriefDto;
import GRWM.backend.dto.teamPlanner.TeamMemberDto;
import GRWM.backend.service.teamplanner.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team-planner")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    /*
    함수명 : addMember
    기능 : 멤버 추가
    매개변수 : Long plannerId, Long MemberId,
    POST /api/team-planner/{plannerId}/member/{memberId}/{memberRole}
    반환값: ResponseEntity<Void>
    */

    @PostMapping("/{plannerId}/member/{memberId}/{memberRole}")
    public ResponseEntity<Void> addMember(@PathVariable Long plannerId,
                                          @PathVariable Long memberId,
                                          @PathVariable String memberRole){
        teamMemberService.addMember(plannerId, memberId, memberRole);
        return ResponseEntity.ok().build();
    }


    /*
    함수명 : getMemberList
    기능 : 멤버 목록 가져오기(탈퇴 멤버는 제외)
    GET /api/team-planner/{plannerId}/member
    매개변수 : plannerId
    반환값 : List<MemberBriefDto> members
    */

    @GetMapping("/{plannerId}/member")
    public List<TeamMemberDto> getMemberList(@PathVariable Long plannerId){
        return teamMemberService.getTeamMemberList(plannerId);
    }



    /*
    함수명 : deleteMember
    기능 : 멤버 삭제(삭제 시 멤버는 남겨두고 status를 withdrawn변경)
    DELETE /api/team-planner/{plannerId}/member/{memberId}
    매개변수 : Long plannerId, Long plannerId
    반환값 : ResponseEntity 204
    */

    @DeleteMapping("/{plannerId}/member/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long plannerId,
                                             @PathVariable Long memberId){
        teamMemberService.deleteMember(plannerId, memberId);
        return ResponseEntity.noContent().build();
    }

    /*
    함수명 : setMemberNickname
    기능 : 멤버 역할/별명 작성
    PUT /api/team-planner/{plannerId}/member/{memberId}/{roleName}
    매개변수 : Long plannerId, Long memberId, String roleName
    * 방장. 멤버 그 역할과는 별개.
    */

    @PutMapping("/{plannerId}/member/{memberId}/{roleName}")
    public ResponseEntity<Void> setMemberNickname(@PathVariable Long plannerId,
                                                  @PathVariable Long memberId,
                                                  @PathVariable String roleName){
            teamMemberService.setMemberNickName(plannerId, memberId, roleName);
        return ResponseEntity.ok().build();
    }


}
