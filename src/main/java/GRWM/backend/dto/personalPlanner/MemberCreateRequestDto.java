package GRWM.backend.dto.personalPlanner;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberCreateRequestDto {

    private String username;

    private String loginId;

    private String password;

    private String email;


    /*
    함수명 : 커스텀 생성자
    기능 : 생성자이다.
    매개변수 : String, String, String, String
    리턴값 : 없음
     */
    public MemberCreateRequestDto(String username, String loginId, String password, String email) {

        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }




}
