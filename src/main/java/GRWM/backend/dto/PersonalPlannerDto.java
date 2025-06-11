package GRWM.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalPlannerDto {

    private Long plannerId;

    private String title;

    private String explanation;

    private String profileImageLink;


    public PersonalPlannerDto() {}

    public PersonalPlannerDto(Long plannerId, String title, String explanation, String profileImageLink) {
        this.plannerId = plannerId;
        this.title = title;
        this.explanation = explanation;
        this.profileImageLink = profileImageLink;


    }
}
