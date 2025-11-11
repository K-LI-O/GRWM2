package GRWM.backend.dto.teamPlanner;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamTodoDto {

    /*
Long todoId
{Long userId, String username, String profileImage} user(중첩 dto 이름)
String content
boolean isCompleted
boolean isPrivate

     */
    private Long todoId;
    private TeamMemberBriefDto user;
    String content;
    boolean isCompleted;
    boolean isPrivate;
}
