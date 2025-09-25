package GRWM.backend.dto.community;


import GRWM.backend.entity.community.CommunityUser;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {

    private String title;

    // private List<String> imageList;

    private String content;

    private String visibility;

    private Long communityId;

}
