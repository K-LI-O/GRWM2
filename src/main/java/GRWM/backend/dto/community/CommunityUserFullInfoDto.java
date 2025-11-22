package GRWM.backend.dto.community;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Builder
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

    private String relationship;

}
