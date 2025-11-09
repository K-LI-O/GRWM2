package GRWM.backend.service.tracker;

import GRWM.backend.dto.studyroom.ExtensionDto;
import GRWM.backend.dto.tracker.CreateRecurringTodoDto;
import GRWM.backend.dto.tracker.RecurringTodoDto;
import GRWM.backend.dto.tracker.RecurringTodoListDto;
import GRWM.backend.dto.tracker.TodoDto;
import GRWM.backend.entity.tracker.TrackerTodo;
import GRWM.backend.repository.tracker.TrackerTodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTodoService {

    private final TrackerTodoRepository trackerTodoRepository;
    private final TaskScheduler taskScheduler;

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
                                                     String type, String status){
        boolean isActive = status.equals("active");
        List<TrackerTodo> todoList = trackerTodoRepository.findByCreatorIdAndIsRecurringTrueAndRepeatRangeAndIsActive(userId, type, isActive);
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

        // 나머지 한달 동안의 어쩌구 생성.
        if(savedTodo.isActive()) {
            generateSchedule(savedTodo);
        }

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

        todo.setTitle(dto.getTodoDto().getTitle());
        todo.setDescription(dto.getTodoDto().getDescription());
        todo.setRepeatRange(dto.getRepeatRange());
        todo.setActive(dto.isActive());

        return dto;
    }

    /*
    name : deleteRecurringTodo
    function :반복 To-Do 삭제
    URL: DELETE /api/users/{userId}/recurring-todos/{recurringId}
    */
    public void deleteRecurringTodo(Long userId, Long recurringId){
        trackerTodoRepository.deleteById(recurringId);
    }

    /*
    name : create
2.5반복 To-Do 자동 생성 (시작일 기준 한 달에 한 번씩 시스템 호출)
URL: POST /api/users/{userId}/recurring-todos/generate
Request: Long userId;
Request Body: { targetDate?: Date; // 생성 대상 날짜 (기본값: 오늘)
}
Response: { generatedTodos: Todo[]; targetDate: Date; }
+ isActive로 계속되는 것들은 한달마다 자동 연장
     */

    @Scheduled(cron = "0 0 0 1 * ?")
    protected void generateRecurringTodo(){
        // 해당하는 객체 리스트 불러오기 isRecurring이 true이고, isActive가 true인 것들, 매월 1일 실행
        List<TrackerTodo> todoList = trackerTodoRepository.findByIsRecurringTrueAndIsActiveTrue();
        // 언제 실행할지 정하기
        for(TrackerTodo tt : todoList){
            generateSchedule(tt);
        }

     }
     // 다만, 1일 이후에 isActive 해제 or 반복 일정 생성 시 다음 달에 반영된다는 점...
    // 이 점은 수정 필요 해당 어쩌구가 들어올 때 이미 실행.

    private void generateSchedule(TrackerTodo tt){
        // 먼슬리인 경우
        if(tt.getRepeatRange().equals("monthly")){
            LocalDate scheduledDateThisMonth = getMonthlySchedule(tt);

            if (!scheduledDateThisMonth.isBefore(LocalDate.now())) {
                createAndSaveTodo(tt, scheduledDateThisMonth);
            }

        } else if(tt.getRepeatRange().equals("weekly")) {// 위클리인 경우
            LocalDate today = LocalDate.now();
            int startDay = today.getDayOfMonth() != 1 ? today.getDayOfMonth() : 1;
            int maxDay = today.lengthOfMonth();
            // 이번 달 1일을 기준으로 한 LocalDate 객체 (withDayOfMonth() 호출을 위해 사용)
            LocalDate baseDate = today.withDayOfMonth(1);

            List<Integer> weeks = tt.getWeekly();
            for (int day = startDay; day <= maxDay; day++) {
                // 현재 순회 중인 날짜 객체 생성
                LocalDate targetDate = baseDate.withDayOfMonth(day);
                // 해당 날짜의 요일
                DayOfWeek dayOfWeek = targetDate.getDayOfWeek();

                // 템플릿에 설정된 반복 요일에 현재 날짜의 요일이 포함되는지 확인
                if (weeks.contains(dayOfWeek.getValue())) {
                    TrackerTodo todo = TrackerTodo.builder()
                            .creatorId(tt.getCreatorId())
                            .title(tt.getTitle())
                            .description(tt.getDescription())
                            .date(targetDate)
                            .isCompleted(false)
                            .isPostponed(false)
                            .isRecurring(false)
                            .build();
                    trackerTodoRepository.save(todo);
                }
            }
        } else { // 날별 반복인 경우
            // 1. 기준 날짜 및 반복 정보 설정
            LocalDate today = LocalDate.now();
            // 현재 달의 마지막 날을 순회 종료 기준으로 설정
            LocalDate currentMonthEnd = today.withDayOfMonth(today.lengthOfMonth());

            LocalDate initialStartDate = tt.getDate(); // 템플릿의 최초 시작일
            int repeatInterval = tt.getRepeatInterval();   // N일 간격 (예: 3일);

            // 3. 최초 시작점 찾기 (이번 달에 생성될 첫 번째 투두 날짜)

            // today와 initialStartDate 사이의 일(Day) 차이 계산
            long daysFromInitial = ChronoUnit.DAYS.between(initialStartDate, today);
            // repeatInterval로 나누었을 때의 나머지
            long remainder = daysFromInitial % repeatInterval;

            // 이번 달에 생성될 투두의 첫 번째 날짜 (initialStartDate 기준 N일 간격으로 계산됨)
            LocalDate nextScheduleDate;
            if (remainder == 0) {
                // A. 오늘(today)이 N일 간격에 정확히 걸리는 날짜임 (today 자체가 생성일)
                nextScheduleDate = today;
            } else {
                // B. 오늘 이후 다음 간격의 날짜를 찾음
                // 다음 간격까지 남은 일 수: (repeatInterval - remainder)
                nextScheduleDate = today.plusDays(repeatInterval - remainder);
            }


            // 3. 투두 생성 및 순회 (오늘 이후 월말까지)
            while (!nextScheduleDate.isAfter(currentMonthEnd)) {

                // ⭐️ 투두 생성 및 저장 로직 실행
                TrackerTodo todo = TrackerTodo.builder()
                        .creatorId(tt.getCreatorId())
                        .title(tt.getTitle())
                        .description(tt.getDescription())
                        .date(nextScheduleDate)
                        .isCompleted(false)
                        .isPostponed(false)
                        .isRecurring(false)
                        .build();
                trackerTodoRepository.save(todo);

                // 다음 생성 예정일로 N일만큼 이동
                nextScheduleDate = nextScheduleDate.plusDays(repeatInterval);
            }
        }
    }

    // 먼슬리 로직에 사용할 날짜 조정 로직
    private LocalDate getMonthlySchedule(TrackerTodo tt){
        LocalDate today = LocalDate.now();
        int maxDayOfMonth = today.lengthOfMonth();
        int targetDay = tt.getDate().getDayOfMonth();

        if (targetDay > maxDayOfMonth) {
            // 설정하려는 일자가 유효 범위를 벗어난 경우
            // 해당 월의 마지막 날로 설정하여 반환
            return LocalDate.now().withDayOfMonth(maxDayOfMonth);
        } else {
            // 유효한 경우, 해당 일자로 설정하여 반환
            return LocalDate.now().withDayOfMonth(targetDay);
        }
    }




    private TodoDto todoToDto(TrackerTodo t){
        TodoDto dto = TodoDto.builder()
                .todoId(t.getId())
                .creatorId(t.getCreatorId())
                .title(t.getTitle())
                .description(t.getDescription())
                .date(t.getDate())
                .isCompleted(t.isCompleted())
                .isPostponed(t.isPostponed())
                .isRecurring(t.isRecurring())
                .build();
        return dto;
    }

    private LocalDate getMonthly(TrackerTodo tt){
        // 이전 답변에서 수정된 안전한 로직을 여기에 사용합니다.
        LocalDate today = LocalDate.now();
        int maxDayOfMonth = today.lengthOfMonth();
        int targetDay = tt.getDate().getDayOfMonth();

        if (targetDay > maxDayOfMonth) {
            return today.withDayOfMonth(maxDayOfMonth);
        } else {
            return today.withDayOfMonth(targetDay);
        }
    }

    // 투두를 생성하고 저장하는 재사용 가능한 로직
    private void createAndSaveTodo(TrackerTodo tt, LocalDate dateToCreate) {
        TrackerTodo todo = TrackerTodo.builder()
                .creatorId(tt.getCreatorId())
                .title(tt.getTitle())
                .description(tt.getDescription())
                .date(dateToCreate)
                .isCompleted(false)
                .isPostponed(false)
                .isRecurring(false) // 생성된 개별 투두는 반복이 아닙니다.
                .build();
        trackerTodoRepository.save(todo);
    }


}
