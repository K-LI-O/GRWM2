package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDto {
    /*
    Long userId,
String username,
String nickname, (맨 처음에는 빈 String)
String profileImage,
String email,
String role,
String status (맨 처음에는 active, 탈퇴 시 withdraw)

     */

    private Long userID;
    private String username;
    private String nickname;
    private String profileImage;
    private String email;
    private String roll;
    private String status;
}
