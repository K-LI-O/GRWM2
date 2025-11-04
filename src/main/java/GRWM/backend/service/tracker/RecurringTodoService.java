package GRWM.backend.service.tracker;

import GRWM.backend.dto.tracker.CreateRecurringTodoDto;
import GRWM.backend.dto.tracker.RecurringTodoDto;
import GRWM.backend.dto.tracker.RecurringTodoListDto;
import GRWM.backend.dto.tracker.TodoDto;
import GRWM.backend.entity.tracker.TrackerTodo;
import GRWM.backend.repository.tracker.TrackerTodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTodoService {

    private final TrackerTodoRepository trackerTodoRepository;

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
    public RecurringTodoListDto getRecurringTodoList(Long userId,
                                                     String type, boolean status){
        List<TrackerTodo> todoList = trackerTodoRepository.findByCreatorIdAndIsRecurringTrueAndRepeatRangeAndIsActive(userId, type, status);
        List<RecurringTodoDto> todoDtos = new ArrayList<>();
        int activeCount = 0;
        for(TrackerTodo t : todoList){
            if(t.isActive()) activeCount++;
            RecurringTodoDto dto = RecurringTodoDto.builder()
                    .todoDto(todoToDto(t))
                    .isActive(t.isActive())
                    .repeatRange(t.getRepeatRange().toString())
                    .build();
            todoDtos.add(dto);
        }
        RecurringTodoListDto result = RecurringTodoListDto.builder()
                .recurringTodos(todoDtos)
                .activeCount(activeCount)
                .totalCount(todoDtos.size())
                .build();

        return result;

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
    public RecurringTodoDto createRecurringTodo(Long userId, CreateRecurringTodoDto dto){
        // 객체 생성
        TrackerTodo todo = TrackerTodo.builder()
                .creatorId(userId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .date(dto.getStartDate())
                .isRecurring(true)
                .repeatRange(dto.getRecurrenceType())
                .repeatInterval(dto.getRecurrenceType().equals("daily") ? dto.getRecurrenceConfig().getInterval() : 0)
                .weekly(dto.getRecurrenceType().equals("weekly") ? dto.getRecurrenceConfig().getWeekly() : new ArrayList<>())
                .monthly(dto.getRecurrenceType().equals("monthly") ? dto.getRecurrenceConfig().getMonthly() : 0)
                .build();
        // 객체 저장
        TrackerTodo savedTodo = trackerTodoRepository.save(todo);

        // 반환
        RecurringTodoDto result = RecurringTodoDto
                .builder()
                .todoDto(todoToDto(savedTodo))
                .repeatRange(savedTodo.getRepeatRange())
                .isActive(savedTodo.isActive())
                .build();
        return result;
    }

    /*
    name : updateRecurringTodo
    function : 반복 To-Do 수정
    URL: PUT /api/users/{userId}/recurring-todos/{recurringId}
    */
    public RecurringTodoDto updateRecurringTodo(Long userId, Long recurringId, RecurringTodoDto dto){
        TrackerTodo todo = trackerTodoRepository.findById(recurringId).orElseThrow();



        return dto;
    }

    /*
    name : deleteRecurringTodo
    function :반복 To-Do 삭제
    URL: DELETE /api/users/{userId}/recurring-todos/{recurringId}
    */

    /*
    name : creat
2.5반복 To-Do 자동 생성 (시스템 호출)
URL: POST /api/users/{userId}/recurring-todos/generate
Request: Long userId;
Request Body: { targetDate?: Date; // 생성 대상 날짜 (기본값: 오늘)
}
Response: { generatedTodos: Todo[]; targetDate: Date; }
+ 그냥 반복 todo 생성 시에 일단 만들어서 DB에 넣어두어도 괜찮을 듯(생성 개수 limit을 정해서).
     */

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
