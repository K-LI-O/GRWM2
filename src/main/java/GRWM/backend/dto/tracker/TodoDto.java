package GRWM.backend.dto.tracker;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDto {
    Long todoId;
    Long creatorId;
    String title;
    String description;
    LocalDate date;
    boolean isCompleted;
    boolean isPostponed;
}
