package GRWM.backend.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private Long postId;

    private Long communityId;

    private String title;

    // private List<String> imageList;

    private String content;

    private String visibility;

}
