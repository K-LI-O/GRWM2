package GRWM.backend.controller.studyroom;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyRoomTodoController {

    /*
    name : getTodoList
    URL: GET /api/study-rooms/{studyRoomId}/todos
    param: Long studyRoomId
    return value : List<StudyRoomTodoDto> todos
    */

    /*
    name : createTodo
    URL: POST /api/study-rooms/{studyRoomId}/todos
    param : Long studyRoomId, CreateTodoDto, userDetails
    return value : StudyRoomTodoDto
    */

    /*
    name : updateTodo
    URL: PUT /api/study-rooms/{studyRoomId}/todos/{todoId}
    param : StudyRoomTodoCreateDto
    return value : StudyRoomTodoDto
    */
    /*
    name : deleteTodo
    URL: DELETE /api/study-rooms/{studyRoomId}/todos/{todoId}
    param : Long studyRoomId, Long todoId
    return value : ResponseEntity 204
    */

    /*
    name : completeTodo
    URL: PATCH /api/study-rooms/{studyRoomId}/todos/{todoId}/complete
    param : Long studyRoomId, Long todoId
    return value: StudyRoomTodoDto
    */

    /*
    name : CreateTodoReaction
    URL: POST /api/study-rooms/{studyRoomId}/todos/{todoId}/reactions
    param : Long studyRoomId, Long todoId
    return value : Long reactionId
    */

    /*
    name : deleteTodoReaction
    url : DELETE /api/study-rooms/{studyRoomId}/todos/{todoId}/reactions/{reactionId}
    param : Long studyRoomId, Long todoId, Long reactionId
    return value : ResponseEntity 204
     */


}
