package GRWM.backend.controller.teamplanner;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamTodoController {

    /*
    name : createTodoList
    function : 투두리스트 생성
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/todo
    param : Long plannerId, Long scheduleId
    return value : Long todoId
    */

    /*
    name : updateTodo
    function : 투두리스트 수정 및 완료 체크
    PUT/api/team-planner/{plannerId}/schedule/{scheduleId}/todo/{todoId}
    param : Long plannerId, Long scheduleId, Long todoId
    return value : TeamTodoUpdateDto
    */

    /*
    name : deleteTodo
    function : 투두리스트 삭제
    DELETE /api/te  m-planner/{plannerId}/schedule/{scheduleId}/todo/{todoId}
    Request: Long plannerId, Long scheduleId, Long todoId
    Response: x
     */
}
