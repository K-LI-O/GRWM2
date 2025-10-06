package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlannerCreateDto {
    /*
    Long creatorId,
String title,
String description,
String profileImage

     */

    private Long creatorId;
    private String title;
    private String description;
    private String profileImage;


}
