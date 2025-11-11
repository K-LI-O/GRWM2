package GRWM.backend.entity.user;

import GRWM.backend.entity.chatroom.ChatRoomMember;
import GRWM.backend.entity.notification.PushToken;
import GRWM.backend.entity.teamplanner.TeamMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Member_ID")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String username;

    // 로그인 및 회원가입

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false, name = "pwd")
    private String password;

    @Column(nullable = false)
    private String email;
    private String profileImageLink;

    @OneToOne(mappedBy = "member")
    private PushToken pushToken;


    // 단체 플래너
    @OneToMany(mappedBy = "member")
    private List<TeamMember> teamMembers = new ArrayList<>();

    // 채팅방
    @OneToMany(mappedBy = "member")
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();


    // 커뮤니티
    @OneToOne
    private CommunityUser communityUser;

    // 생성자

    public Member(String username, String loginId, String password, String email, CommunityUser cu){

        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.communityUser = cu;
    }

    public Member(String username, String loginId, String password, String email){

        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }

}
