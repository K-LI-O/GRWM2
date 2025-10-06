package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

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
