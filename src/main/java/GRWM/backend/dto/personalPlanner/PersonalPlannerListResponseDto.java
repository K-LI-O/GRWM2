package GRWM.backend.dto.personalPlanner;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalPlannerListResponseDto {
    private Long userId;

    private String title;

    private String explanation;

    private String profileImageLink;


    public PersonalPlannerListResponseDto() {}

    public PersonalPlannerListResponseDto(Long userId, String title, String explanation, String profileImageLink) {
        this.userId = userId;
        this.title = title;
        this.explanation = explanation;
        this.profileImageLink = profileImageLink;


    }

}
