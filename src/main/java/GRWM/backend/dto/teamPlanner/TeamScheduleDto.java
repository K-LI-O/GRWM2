package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamScheduleDto {

    /*
{Long userId, String username, String profileImage} creator
String title;
{Long categoryId, String categoryName, String color} category(중첩 dto 이름),
private LocalDateTime startDateTime,
LocalDateTime finishDateTime,
String location,
String memo,
String editorRange,
List<MemberDto> members, (참여자 목록),
List<TodoDto> todoList

     */
    private TeamMemberBriefDto creator;
    String title;
    private TeamCategoryDto category;
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;
    private String location;
    private String memo;
    private String editorRange;
    private List<TeamMemberBriefDto> members;
    private List<TeamTodoDto> todoList;
    



}
