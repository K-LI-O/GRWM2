package GRWM.backend.controller.teamplanner;

import GRWM.backend.dto.teamPlanner.TeamPlannerCreateDto;
import GRWM.backend.dto.teamPlanner.TeamPlannerDto;
import GRWM.backend.dto.teamPlanner.TeamPlannerUpdateDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.teamplanner.TeamPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team-planner")
public class TeamPlannerController {

    private final TeamPlannerService plannerService;

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

    @PostMapping("/create")
    public ResponseEntity<Long> createPlanner(@RequestBody TeamPlannerCreateDto dto,
                                              @AuthenticationPrincipal CustomUserDetails userDetails){

        return ResponseEntity.ok(plannerService.createTeamPlanner(dto, userDetails.getUserId()));
    }

    /*
    name : getPlannerList
    function : 플래너 목록 조회
    GET /api/team-planner/list
    param : x
    return value : List<TeamPlannerDto>
    */

    @GetMapping("/list")
    public List<TeamPlannerDto> getPlannerList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return plannerService.getPlannerList(userDetails.getUserId());
    }

    /*
    name : updatePlanner
    function : 플래너 업데이트
    PUT /api/team-planner/{plannerId}/update
    param : Long plannerId
    return value : TeamPlannerDto
    */

    @PutMapping("/{plannerId}/update")
    public TeamPlannerDto updatePlanner(@PathVariable Long plannerId,
                                        @RequestBody TeamPlannerUpdateDto dto){
        return plannerService.updatePlanner(plannerId, dto);
    }


    /*
    name : deletePlanner
    function : 플래너 삭제
    DELETE /api/personal-planner/{plannerId}/delete
    param : Long plannerId
    return value : x
     */
    @DeleteMapping("/{plannerId}/delete")
    public ResponseEntity deletePlanner(@PathVariable Long plannerId) {
        plannerService.deletePlanner(plannerId);
        return ResponseEntity.noContent().build();
    }

}
