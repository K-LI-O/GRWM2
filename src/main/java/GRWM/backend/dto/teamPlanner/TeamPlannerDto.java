package GRWM.backend.dto.teamPlanner;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPlannerDto {

    /*
    Long plannerId,
String title,
String description,
String profileImageLink,
List<TeamMemberDto> members

     */

    private Long plannerId;
    private String title;
    private String description;
    private String  profileImageLink;
    private List<TeamMemberBriefDto> members;
}
