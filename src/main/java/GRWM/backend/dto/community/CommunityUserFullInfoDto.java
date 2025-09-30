package GRWM.backend.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityUserFullInfoDto {

    private CommunityUserBriefDto User;

    private String description;
    private String bannerImage;

    private int postCount;

    private int followerCount;
    private int followingCount;

    private int archivedBadgeCount; // 뱃지 정보;
    private Long pinnedPostId;


}
