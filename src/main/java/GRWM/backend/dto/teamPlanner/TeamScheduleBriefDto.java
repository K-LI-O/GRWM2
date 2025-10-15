package GRWM.backend.dto.teamPlanner;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamScheduleBriefDto {
    /*
Long scheduleId
{Long userId, String username, String profileImage} creator
String title;
{Long categoryId, String categoryName, String color} category(중첩 dto 이름),
private LocalDateTime startDateTime,
LocalDateTime finishDateTime,

     */

    private Long scheduleId;
    private TeamMemberBriefDto creator;
    private String title;
    private TeamCategoryDto category;
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;


}
