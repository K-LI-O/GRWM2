package GRWM.backend.controller.teamplanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/team-planner/")
public class TeamMemberController {

    /*
    함수명 : addMember
    기능 : 멤버 추가
    매개변수 : Long plannerId, Long MemberId,
    POST /api/team-planner/{plannerId}/member/{memberId}
    반환값: ResponseEntity<Void>
*/



    /*
    함수명 : getMemberList
    기능 : 멤버 목록 가져오기(탈퇴 멤버는 제외)
    GET /api/team-planner/{plannerId}/member
    매개변수 : plannerId
    반환값 : List<MemberBriefDto> members
*/



    /*
    함수명 : deleteMember
    기능 : 멤버 삭제(삭제 시 멤버는 남겨두고 status를 withdrawn변경)
    DELETE /api/team-planner/{plannerId}/member/{memberId}
    매개변수 : Long plannerId, Long plannerId
    반환값 : ResponseEntity 204
*/


    /*
    함수명 : setMemberNickname
    기능 : 멤버 역할/별명 작성
    PUT /api/team-planner/{plannerId}/member/{memberId}/{roleName}
    매개변수 : Long plannerId, Long memberId, String roleName
    * 방장. 멤버 그 역할과는 별개.
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
