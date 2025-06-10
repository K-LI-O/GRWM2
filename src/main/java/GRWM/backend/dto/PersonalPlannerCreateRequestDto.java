package GRWM.backend.dto;

import GRWM.backend.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
public class PersonalPlannerCreateRequestDto {

    private String creatorId;

    private String title;

    private String explanation;

    private String profileImageLink;


    public PersonalPlannerCreateRequestDto() {}

    public PersonalPlannerCreateRequestDto(String creatorId, String title, String explanation, String profileImageLink) {
        this.creatorId = creatorId;
        this.title = title;
        this.explanation = explanation;
        this.profileImageLink = profileImageLink;


    }


}



