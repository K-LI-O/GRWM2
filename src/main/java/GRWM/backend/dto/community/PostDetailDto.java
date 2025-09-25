package GRWM.backend.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDetailDto {

    // + Like 수, 코멘트 수

    private String title;

    // private List<String> imageList;

    private String content;

    private String visibility;

    private Long communityId;

}
