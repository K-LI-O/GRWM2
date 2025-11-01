package GRWM.backend.dto.tracker;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTodoDto {
    String title;
    String description;
}
