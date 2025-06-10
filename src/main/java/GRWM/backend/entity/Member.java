package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column
    private String username;

    // 로그인 및 회원가입

    @Column
    private String loginId;

    @Column
    private String password;

    @Column
    private String email;

    // 개인 플래너

    // 단체 플래너

    // 채팅방

    private String chatName;

    // 커뮤니티

    // 생성자

    public Member(String username, String loginId, String password, String email){

        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.email = email;

    }

}
