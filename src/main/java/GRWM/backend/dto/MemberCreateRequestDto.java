package GRWM.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateRequestDto {

    private String username;

    private String login_id;

    private String password;

    private String email;

    public MemberCreateRequestDto() {}


    /*
    함수명 : 커스텀 생성자
    기능 : 생성자이다.
    매개변수 : String, String, String, String
    리턴값 : 없음
     */
    public MemberCreateRequestDto(String username, String login_id, String password, String email) {

        this.username = username;
        this.login_id = login_id;
        this.password = password;
        this.email = email;
    }




}
