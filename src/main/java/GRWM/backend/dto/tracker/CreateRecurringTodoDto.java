package GRWM.backend.dto.tracker;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecurringTodoDto {
    String title; // To-Do 제목
    String description; // To-Do 설명
    String recurrenceType; // : "daily" | "weekly" | "monthly"; // 반복 타입
    RecurrenceConfig recurrenceConfig;
    LocalDate startDate; // 시작일
}


