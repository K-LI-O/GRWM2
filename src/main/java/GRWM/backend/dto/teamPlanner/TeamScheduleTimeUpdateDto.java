package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamScheduleTimeUpdateDto {

    /*
LocalDateTime startDateTime,
LocalDateTime finishDateTime
     */
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;
}
