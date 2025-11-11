package GRWM.backend.dto.teamPlanner;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamCategoryDto {
// {Long categoryId, String categoryName, String color
    private Long categoryId;
    private String categoryName;
    private String color;

}
