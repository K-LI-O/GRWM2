package GRWM.backend.dto.tracker;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTodoDto {
    LocalDate date;
    String title;
    String description;
}
