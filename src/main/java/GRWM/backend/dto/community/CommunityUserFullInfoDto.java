package GRWM.backend.dto.community;

import GRWM.backend.entity.Member;
import GRWM.backend.entity.community.CommunityUserHashtag;
import GRWM.backend.entity.community.Following;
import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.community.UserBadge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.List;

public class CommunityUserDto {

    private Long communityId;

    private String nickname;

    private String description;

    private String profileImage;

    private int following;
    private int follower;

    private int badge; // 뱃지 정보;

    private List<String> subscribedHashtagList;

}
