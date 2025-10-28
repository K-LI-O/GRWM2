package GRWM.backend.controller.teamplanner;

import GRWM.backend.dto.teamPlanner.TeamTodoDto;
import GRWM.backend.dto.teamPlanner.TeamTodoUpdateDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.teamplanner.TeamTodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TeamTodoController {
    private final TeamTodoService teamTodoService;

    /*
    name : createTodoList
    function : 투두리스트 생성
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/todo
    param : Long plannerId, Long scheduleId, TeamTodoDto
    return value : Long todoId
    */
    @PostMapping("/api/team-planner/{plannerId}/schedule/{scheduleId}/todo")
    public Long createTodoList(@PathVariable Long plannerId, @PathVariable Long scheduleId,
                               @RequestBody TeamTodoDto dto){
        return teamTodoService.createTodoList(plannerId, scheduleId, dto);
    }


    /*
    name : updateTodo
    function : 투두리스트 수정 및 완료 체크
    PUT/api/team-planner/{plannerId}/schedule/{scheduleId}/todo/{todoId}
    param : Long plannerId, Long scheduleId, Long todoId
    return value : TeamTodoUpdateDto
    */
    @PutMapping("/api/team-planner/{plannerId}/schedule/{scheduleId}/todo/{todoId}")
    public TeamTodoUpdateDto updateTodo(@PathVariable Long plannerId,
                                        @PathVariable Long scheduleId,
                                        @PathVariable Long todoId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody TeamTodoUpdateDto dto){
        return teamTodoService.updateTodo(plannerId, scheduleId, todoId, userDetails.getUserId(), dto);
    }

    /*
    name : deleteTodo
    function : 투두리스트 삭제
    DELETE /api/team-planner/{plannerId}/schedule/{scheduleId}/todo/{todoId}
    Request: Long plannerId, Long scheduleId, Long todoId
    Response: x
     */

    @DeleteMapping("/api/team-planner/{plannerId}/schedule/{scheduleId}/todo/{todoId}")
    public void deleteTodo(@PathVariable Long plannerId,@PathVariable Long scheduleId, @PathVariable Long todoId){
        teamTodoService.deleteTodo(plannerId, scheduleId, todoId);
    }

}
