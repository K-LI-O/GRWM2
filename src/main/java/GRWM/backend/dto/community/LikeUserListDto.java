package GRWM.backend.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LikeUserListDto {

    private List<CommunityUserBriefDto> likedUsers;

}
