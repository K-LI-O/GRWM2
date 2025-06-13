package GRWM.backend.dto.personalPlanner;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalScheduleDateTimeDto {

    private LocalDateTime startDateTime;

    private LocalDateTime finishDateTime;

}
