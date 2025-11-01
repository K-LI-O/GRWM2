package GRWM.backend.service.tracker;

import GRWM.backend.dto.tracker.CreateTodoDto;
import GRWM.backend.dto.tracker.TodoDto;
import GRWM.backend.entity.tracker.TrackerTodo;
import GRWM.backend.repository.tracker.TrackerTodoRepository;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackerTodoService {

    private final TrackerTodoRepository trackerTodoRepository;
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
    public List<TodoDto> getTodoList(Long userId, LocalDate date, String status,
                               int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<TrackerTodo> todoPage = trackerTodoRepository.findByCreatorId(userId, pageable);
        List<TrackerTodo> todoList = todoPage.getContent();

        List<TodoDto> result = new ArrayList<>();
        for(TrackerTodo t : todoList){
            TodoDto dto = TodoDto.builder()
                    .todoId(t.getId())
                    .creatorId(t.getCreatorId())
                    .title(t.getTitle())
                    .description(t.getDescription())
                    .date(t.getDate())
                    .isCompleted(t.isCompleted())
                    .isPostponed(t.isPostponed())
                    .build();
            result.add(dto);
        }
        return result;
    }

    /*
    name : createTodo
    function : 개인 To-Do 작성
    URL: POST /api/users/{userId}/todos
    param : Long userId, CreateTodoDto
    Response: TodoDto
    */
    public TodoDto createTodo(Long userId, CreateTodoDto dto){
        // 객체 생성
        TrackerTodo todo = TrackerTodo.builder()
                .creatorId(userId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
        TrackerTodo savedTodo = trackerTodoRepository.save(todo);

        return todoToDto(savedTodo);
    }

    /*
    name : updateTodo
    function : 개인 To-Do 수정
    URL: PUT /api/users/{userId}/todos/{todoId}
    param : Long userId, Long todoId,
    Request Body: TodoDto
    return value : TodoDto
    */
    public TodoDto updateTodo(Long userId, Long todoId, TodoDto dto){
        // 객체 생성
        TrackerTodo todo = trackerTodoRepository.findById(todoId).orElseThrow();
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setDate(dto.getDate());
        todo.setCompleted(dto.isCompleted());
        todo.setPostponed(dto.isPostponed());
        TrackerTodo savedTodo = trackerTodoRepository.save(todo);

        return todoToDto(savedTodo);
    }

    /*
    name : deleteTodo
    function : 개인 To-Do 삭제
    URL: DELETE /api/users/{userId}/todos/{todoId}
    param : Long userId, Long todoId
    */
    public void deleteDto(Long userId, Long todoId){
        TrackerTodo todo = trackerTodoRepository.findById(todoId).orElseThrow();
        trackerTodoRepository.delete(todo);
    }

    /*
    name : completeTodo
    function : 개인 To-Do 완료 처리
    URL: PATCH /api/users/{userId}/todos/{todoId}/complete
    param : Long userId, Long todoId
    return value : TodoDto
     */
    public TodoDto completeTodo(Long userId, Long todoId){
        TrackerTodo todo = trackerTodoRepository.findById(todoId).orElseThrow();

        todo.setCompleted(!todo.isCompleted());
        TrackerTodo savedTodo = trackerTodoRepository.save(todo);

        return todoToDto(savedTodo);
    }


    // ======= private logics ======= //

    public TodoDto todoToDto(TrackerTodo t){
        TodoDto dto = TodoDto.builder()
                .todoId(t.getId())
                .creatorId(t.getCreatorId())
                .title(t.getTitle())
                .description(t.getDescription())
                .date(t.getDate())
                .isCompleted(t.isCompleted())
                .isPostponed(t.isPostponed())
                .build();
        return dto;
    }
}
