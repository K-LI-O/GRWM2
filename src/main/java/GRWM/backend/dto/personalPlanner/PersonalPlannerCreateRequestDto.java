package GRWM.backend.dto.personalPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalPlannerCreateRequestDto {

    private Long creatorId;

    private String title;

    private String explanation;

    private String profileImage;

}



