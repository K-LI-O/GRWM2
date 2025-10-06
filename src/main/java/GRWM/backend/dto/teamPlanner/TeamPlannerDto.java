package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlannerDto {

    /*
    Long plannerId,
String title,
String description,
String profileImageLink,
List<MemberDto> members

     */

    private Long plannerId;
    private String title;
    private String description;
    private String  profileImageLink;
    private List<TeamMemberDto> members;
}
