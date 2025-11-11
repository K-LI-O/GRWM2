package GRWM.backend.dto.teamPlanner;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamMemberBriefDto {
    // {Long userId, String username, String profileImage}
    private Long userId; // 탈퇴한 멤버여도 Id는 유지.
    private String username; // 탈퇴한 멤버라면 "탈퇴한 사용자" 로 바꿔서 전달 예정.
    private String profileImage;
    private String status; // 탈퇴한 멤버인지 표시; 탈퇴한 멤버라면 탈퇴한 사용자로 표시
}
