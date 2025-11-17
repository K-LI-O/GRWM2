package GRWM.backend.dto.teamPlanner;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TeamScheduleSearchByMemberDto {
    List<TeamScheduleBriefDto> createdSchedules;
    List<TeamScheduleBriefDto> joinedSchedules;

}
