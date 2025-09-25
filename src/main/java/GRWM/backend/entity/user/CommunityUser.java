package GRWM.backend.entity.community;

import GRWM.backend.entity.Member;
import GRWM.backend.entity.chatroom.ChatRoomCommunity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CommunityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private String description;

    private String profileImage;

    @OneToOne
    private Member member;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;

    @OneToMany(mappedBy = "follower")
    private List<Following> followingList;

    @OneToMany(mappedBy = "following")
    private List<Following> followerList;

    @OneToMany(mappedBy = "user")
    private List<UserBadge> userBadgeList;

    @OneToMany(mappedBy = "user")
    private List<CommunityUserHashtag> cuHashtagList;


    @OneToMany(mappedBy = "communityUser")
    private List<ChatRoomCommunity> chatRoomCommunityList;





    public CommunityUser(String nickname, String profileImage, String description, Member member){
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.description = description;
        this.member = member;
    }


}
