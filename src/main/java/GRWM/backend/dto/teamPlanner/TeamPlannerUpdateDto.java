package GRWM.backend.dto.teamPlanner;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPlannerUpdateDto {
    private String title;
    private String description;
    private String profileImage;

}
