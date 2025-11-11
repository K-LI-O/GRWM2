package GRWM.backend.dto.teamPlanner;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeVoteShowDto {
    /*
LocalDate date,
LocalTime slotStart,
LocalTime slotEnd,
int overlapCount,
double overlapPercentage
     */

    private LocalDate date;
    private LocalTime slotStart;
    private LocalTime slotEnd;
    int overlapCount;
    double overlapPercentage;
}
