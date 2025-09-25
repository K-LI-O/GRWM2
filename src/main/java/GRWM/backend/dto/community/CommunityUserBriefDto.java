package GRWM.backend.dto.community;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityUserBriefDto {

    private Long communityId;

    private String nickname;

    private String profileImage;

    //private String description;

}
