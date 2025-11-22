package GRWM.backend.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginTokenResponse {

    private String accessToken;
    private String tokenType; // "Bearer"
    private String username; // 사용자 이름 반환
    private Long userId;
    private String communityNickname;
}
