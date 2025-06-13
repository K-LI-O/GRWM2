package GRWM.backend.dto.personalPlanner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalPlannerCreateRequestDto {

    private Long creatorId;

    private String title;

    private String explanation;

    private String profileImageLink;


    public PersonalPlannerCreateRequestDto() {}

    public PersonalPlannerCreateRequestDto(Long creatorId, String title, String explanation, String profileImageLink) {
        this.creatorId = creatorId;
        this.title = title;
        this.explanation = explanation;
        this.profileImageLink = profileImageLink;


    }


}



