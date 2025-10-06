package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
