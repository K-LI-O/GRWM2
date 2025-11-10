package GRWM.backend.controller.tracker;

import GRWM.backend.dto.tracker.CreateRecurringTodoDto;
import GRWM.backend.dto.tracker.RecurringTodoDto;
import GRWM.backend.dto.tracker.RecurringTodoListDto;
import GRWM.backend.dto.tracker.TodoDto;
import GRWM.backend.service.tracker.RecurringTodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecurringTodoController {

    private final RecurringTodoService service;
    /*
    name : getRecurringTodoList
    function : 반복 To-Do 목록 조회
    URL: GET /api/users/{userId}/recurring-todos
    param : Long userId,
Query Parameters: {
type?: "daily" | "weekly" | "monthly"; // 반복 타입
status?: "active" | "inactive" ; // 활성 상태
}
    return value : {
List<RecurringTodo> recurringTodos;
int activeCount;
int totalCount; }
    */
    @GetMapping("/api/users/{userId}/recurring-todos")
    public RecurringTodoListDto getRecurringTodoList(@PathVariable Long userId,
                                                     @RequestParam String status){
        return service.getRecurringTodoList(userId, status);
    }

    /*
    name : createRecurringTodo
    function : 반복 To-Do 생성
    URL: POST /api/users/{userId}/recurring-todos
    param : Long userId,
Body: {
title: string; // To-Do 제목
description: string; // To-Do 설명
recurrenceType: "daily" | "weekly" | "monthly"; // 반복 타입
recurrenceConfig: {
daily?: { repeatInterval: number; // 며칠마다 };
weekly?: { daysOfWeek: number[]; // 요일 (0=일요일, 6=토요일)};
monthly?: { dayOfMonth: number; // 몇 일에 };
};
startDate: Date; // 시작일
}
    return value : { recurringTodo: RecurringTodo}
    */
    @PostMapping("/api/users/{userId}/recurring-todos")
    public RecurringTodoDto createRecurringTodo(@PathVariable Long userId,
                                                @RequestBody CreateRecurringTodoDto dto){
        return service.createRecurringTodo(userId, dto);
    }
    /*
    name : updateRecurringTodo
    function : 반복 To-Do 수정
    URL: PUT /api/users/{userId}/recurring-todos/{recurringId}
    */
    @PutMapping("/api/users/{userId}/recurring-todos/{recurringId}")
    public RecurringTodoDto updateRecurringTodo(@PathVariable Long userId,
                                                @PathVariable Long recurringId,
                                                @RequestBody RecurringTodoDto dto){
        return service.updateRecurringTodo(userId, recurringId, dto);
    }

    /*
    name : deleteRecurringTodo
    function :반복 To-Do 삭제
    URL: DELETE /api/users/{userId}/recurring-todos/{recurringId}
    */
    @DeleteMapping("/api/users/{userId}/recurring-todos/{recurringId}")
    public ResponseEntity<Void> deleteRecurringTodo(@PathVariable Long userId,
                                                    @PathVariable Long recurringId){
        service.deleteRecurringTodo(userId, recurringId);
        return ResponseEntity.noContent().build();
    }

}
