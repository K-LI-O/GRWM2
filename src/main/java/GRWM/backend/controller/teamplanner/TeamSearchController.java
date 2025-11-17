package GRWM.backend.controller.teamplanner;

import GRWM.backend.dto.teamPlanner.TeamScheduleBriefDto;
import GRWM.backend.dto.teamPlanner.TeamScheduleSearchByMemberDto;
import GRWM.backend.entity.teamplanner.TeamSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamSearchController {

    private final TeamSearchService service;

    /*
    name : searchBykeyword
    function : 키워드로 일정 검색
    url : GET /api/team-planner/{plannerId}/search?keyword={keyword}
    Request: Long plannerId, String keyword
    Response: List<TeamScheduleBriefDto>
    */

    @GetMapping(value = "/api/team-planner/{plannerId}/search", params = "keyword")
    public List<TeamScheduleBriefDto> searchByKeyword(
            @PathVariable Long plannerId,
            @RequestParam String keyword
    ) {
        return service.searchByKeyword(plannerId, keyword);
    }

    // 2. memberId 파라미터가 있을 때만 이 메서드 호출
    @GetMapping(value = "/api/team-planner/{plannerId}/search", params = "userId")
    public TeamScheduleSearchByMemberDto searchByMember(
            @PathVariable Long plannerId,
            @RequestParam Long userId
    ) {
        return service.searchByMember(plannerId, userId);
    }


    /*
    name : searchByMember
    function : 사용자별로 생성일정 및 참여일정 검색
    url : GET /api/team-planner/{plannerId}/search?userId = {userId}
    Request: Long plannerId, userId
    Response:
{
List<TeamScheduleBriefDto> createdSchedules, (사용자가 생성한 스케줄)
List<TeamScheduleBriefDto> joinedSchedules (사용자가 생성하진 않았지만 참여)
}
    */


}
