package GRWM.backend.controller.tracker;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecurringTodoController {

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

    /*
    name : updateRecurringTodo
    function : 반복 To-Do 수정
    URL: PUT /api/users/{userId}/recurring-todos/{recurringId}
    */

    /*
    name : deleteRecurringTodo
    function :반복 To-Do 삭제
    URL: DELETE /api/users/{userId}/recurring-todos/{recurringId}
    */

    /*
    name : create
2.5반복 To-Do 자동 생성 (시스템 호출)
URL: POST /api/users/{userId}/recurring-todos/generate
Request: Long userId;
Request Body: { targetDate?: Date; // 생성 대상 날짜 (기본값: 오늘)
}
Response: { generatedTodos: Todo[]; targetDate: Date; }
+ 그냥 반복 todo 생성 시에 일단 만들어서 DB에 넣어두어도 괜찮을 듯(생성 개수 limit을 정해서).

     */
}
