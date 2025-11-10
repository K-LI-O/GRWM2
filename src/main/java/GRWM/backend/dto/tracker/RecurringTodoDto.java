package GRWM.backend.dto.tracker;

import GRWM.backend.entity.tracker.Range;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecurringTodoDto {
    private Long recurringId;
    private TodoDto todoDto;
    private String repeatRange;
    private boolean isActive;
}
