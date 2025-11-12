package GRWM.backend.dto.tracker;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RecurringTodoUpdateDto {
    private String title;
    private String description;
    private LocalDate startDate;
    private boolean isActive;

    private String repeatRange;
    private int daily;
    private List<Integer> weekly;
    private int monthly;

}
