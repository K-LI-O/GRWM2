package GRWM.backend.controller.studyroom;

import GRWM.backend.dto.studyroom.StudyRoomTodoCreateDto;
import GRWM.backend.dto.studyroom.StudyRoomTodoDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.studyroom.StudyRoomTodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyRoomTodoController {

    private final StudyRoomTodoService service;

    /*
    name : getTodoList
    URL: GET /api/study-rooms/{studyRoomId}/todos
    param: Long studyRoomId
    return value : List<StudyRoomTodoDto> todos
    */
    @GetMapping("/api/study-rooms/{studyRoomId}/todos")
    public List<StudyRoomTodoDto> getTodoList(@PathVariable Long studyRoomId){
        return service.getTodoList(studyRoomId);
    }
    /*
    name : createTodo
    URL: POST /api/study-rooms/{studyRoomId}/todos
    param : Long studyRoomId, CreateTodoDto, userDetails
    return value : StudyRoomTodoDto
    */
    @PostMapping("/api/study-rooms/{studyRoomId}/todos")
    public StudyRoomTodoDto createTodo(@PathVariable Long studyRoomId,
                                       @RequestBody StudyRoomTodoCreateDto dto,
                                       @AuthenticationPrincipal CustomUserDetails userDetails){
        return service.createTodo(studyRoomId, dto, userDetails.getCommunityUserId());
    }

    /*
    name : updateTodo
    URL: PUT /api/study-rooms/{studyRoomId}/todos/{todoId}
    param : StudyRoomTodoCreateDto
    return value : StudyRoomTodoDto
    */
    @PutMapping("/api/study-rooms/{studyRoomId}/todos/{todoId}")
    public StudyRoomTodoDto updateTodo(@PathVariable Long studyRoomId, @PathVariable Long todoId,
                                       @RequestBody StudyRoomTodoCreateDto dto){
        return service.updateTodo(studyRoomId, todoId, dto);
    }

    /*
    name : deleteTodo
    URL: DELETE /api/study-rooms/{studyRoomId}/todos/{todoId}
    param : Long studyRoomId, Long todoId
    return value : ResponseEntity 204
    */
    @DeleteMapping("/api/study-rooms/{studyRoomId}/todos/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long studyRoomId, @PathVariable Long todoId){
        service.deleteTodo(studyRoomId, todoId);
        return ResponseEntity.noContent().build();
    }

    /*
    name : completeTodo
    URL: PATCH /api/study-rooms/{studyRoomId}/todos/{todoId}/complete
    param : Long studyRoomId, Long todoId
    return value: StudyRoomTodoDto
    */
    @PatchMapping("/api/study-rooms/{studyRoomId}/todos/{todoId}/complete")
    public StudyRoomTodoDto completeTodo(@PathVariable Long studyRoomId,
                                         @PathVariable Long todoId){
        return service.completeTodo(studyRoomId, todoId);
    }

    /*
    name : CreateTodoReaction
    URL: POST /api/study-rooms/{studyRoomId}/todos/{todoId}/reactions
    param : Long studyRoomId, Long todoId
    return value : Long reactionId
    */
    @PostMapping("/api/study-rooms/{studyRoomId}/todos/{todoId}/reactions")
    public Long createTodoReaction(@PathVariable Long studyRoomId,
                                   @PathVariable Long todoId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        return service.createTodoReaction(studyRoomId, todoId, userDetails.getCommunityUserId());
    }

    /*
    name : deleteTodoReaction
    url : DELETE /api/study-rooms/{studyRoomId}/todos/{todoId}/reactions/{reactionId}
    param : Long studyRoomId, Long todoId, Long reactionId
    return value : ResponseEntity 204
     */
    @DeleteMapping("/api/study-rooms/{studyRoomId}/todos/{todoId}/reactions/{reactionId}")
    public void deleteTodoReaction(@PathVariable Long studyRoomId,
                                   @PathVariable Long todoId,
                                   @PathVariable Long reactionId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        service.deleteTodoReaction(studyRoomId, todoId, reactionId, userDetails.getCommunityUserId());
    }


}
