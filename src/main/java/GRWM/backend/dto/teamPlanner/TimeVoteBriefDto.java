package GRWM.backend.dto.teamPlanner;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeVoteBriefDto {
    private Long id;
    private String title;
    private List<LocalDate> voteRange;
    private LocalDateTime finishTime;
}
