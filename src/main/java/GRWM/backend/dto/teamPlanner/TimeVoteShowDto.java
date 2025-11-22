package GRWM.backend.dto.teamPlanner;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    @Builder.Default
    private List<TeamMemberBriefDto> voters = new ArrayList<>();

    public void addMember(TeamMemberBriefDto member){
        voters.add(member);
    }
}
