package GRWM.backend.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommunityUserCreateDto {

    private Long userId; // 커뮤니티 유저 아이디와 다른 것; 기존 멤버 아이디;

    private String nickname;

    private String description;

    private String profileImage;

}
