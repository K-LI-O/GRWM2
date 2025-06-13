package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
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

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    // 개인 플래너

    // 단체 플래너

    // 채팅방

    @OneToMany(mappedBy = "member")
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();


    // 커뮤니티

    // 생성자

    public Member(String username, String loginId, String password, String email){

        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.email = email;

    }

}
