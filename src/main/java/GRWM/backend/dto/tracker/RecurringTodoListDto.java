package GRWM.backend.dto.tracker;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecurringTodoListDto {
    List<RecurringTodoDto> recurringTodos;
    int activeCount;
    int totalCount;
}
