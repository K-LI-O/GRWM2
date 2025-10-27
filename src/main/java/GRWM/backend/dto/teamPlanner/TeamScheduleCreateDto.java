package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamScheduleCreateDto {
    /*
{
Long plannerId,
Optional<Long> categoryId, (Long 타입 또는 null)
String title,
LocalDateTime startDateTime,
LocalDateTime finishDateTime,
String location
String memo
}
     */
    private Long plannerId;
    private Optional<Long> categoryId;
    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;
    private String location;
    private String memo;
    private String editorRange;
}
