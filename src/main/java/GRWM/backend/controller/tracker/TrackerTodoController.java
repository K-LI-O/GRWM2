package GRWM.backend.controller.tracker;

import GRWM.backend.dto.tracker.CreateTodoDto;
import GRWM.backend.dto.tracker.TodoDto;
import GRWM.backend.service.tracker.TrackerTodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrackerTodoController {

    private final TrackerTodoService trackerTodoService;

    /*
    name : getTodoList
    function : To-Do 목록 조회(Pageable 객체 필요)
    URL: GET /api/users/{userId}/todos
    param : Long userId,
Query Parameters: {
LocalDate(String) date,
String status: "uncompleted" | "completed" ; // 상태 필터
page?: number; // 페이지 번호
limit?: number; // 페이지당 개수
}
    return value : TodoDto
    */
    @GetMapping("/api/users/{userId}/todos")
    public List<TodoDto> getTodoList(@PathVariable Long userId, @RequestParam LocalDate date,
                                     @RequestParam String status, @RequestParam int page,
                                     @RequestParam int limit){
        return trackerTodoService.getTodoList(userId, date, status, page, limit);
    }

    /*
    name : createTodo
    function : 개인 To-Do 작성
    URL: POST /api/users/{userId}/todos
    param : Long userId, CreateTodoDto
    Response: TodoDto
    */
    @PostMapping("/api/users/{userId}/todos")
    public TodoDto createTodo(@PathVariable Long userId, @RequestBody CreateTodoDto dto){
        return trackerTodoService.createTodo(userId, dto);
    }

    /*
    name : updateTodo
    function : 개인 To-Do 수정
    URL: PUT /api/users/{userId}/todos/{todoId}
    param : Long userId, Long todoId,
    Request Body: TodoDto
    return value : TodoDto
    */
    @PutMapping("/api/users/{userId}/todos/{todoId}")
    public TodoDto updateTodo(@PathVariable Long userId, @PathVariable Long todoId,
                              @RequestBody TodoDto dto){
        return trackerTodoService.updateTodo(userId, todoId, dto);
    }

    /*
    name : deleteTodo
    function : 개인 To-Do 삭제
    URL: DELETE /api/users/{userId}/todos/{todoId}
    param : Long userId, Long todoId
    */
    @DeleteMapping("/api/users/{userId}/todos/{todoId}")
    public void deleteTodo(@PathVariable Long userId, @PathVariable Long todoId){
        trackerTodoService.deleteDto(userId, todoId);
    }

    /*
    name : completeTodo
    function : 개인 To-Do 완료 처리
    URL: PATCH /api/users/{userId}/todos/{todoId}/complete
    param : Long userId, Long todoId
    return value : TodoDto
     */
    @PatchMapping("/api/users/{userId}/todos/{todoId}/complete")
    public TodoDto completeTodo(@PathVariable Long userId, @PathVariable Long todoId){
        return trackerTodoService.completeTodo(userId, todoId);
    }
}
