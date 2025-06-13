package GRWM.backend.dto.personalPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalPlannerDto {

    private Long plannerId;

    private String title;

    private String explanation;

    private String profileImageLink;

}
