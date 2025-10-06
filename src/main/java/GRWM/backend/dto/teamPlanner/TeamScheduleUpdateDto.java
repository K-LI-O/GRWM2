package GRWM.backend.dto.teamPlanner;

import java.time.LocalDateTime;

public class TeamScheduleUpdateDto {
    /*
{
String title;
{Long categoryId, String categoryName, String color} category(중첩 dto 이름),
private LocalDateTime startDateTime,
LocalDateTime finishDateTime,
String location,
String memo,
String editorRange
}
     */
    String title;
    TeamCategoryDto category;
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;
    private String location;
    private String memo;
    private String editorRange;


}
