package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class TeamTodoUpdateDto {

    /*

{
String content,
boolean isComplete,
boolean isPrivate
}

     */
    private String content;
    private boolean isComplete;
    private boolean isPrivate;

}
