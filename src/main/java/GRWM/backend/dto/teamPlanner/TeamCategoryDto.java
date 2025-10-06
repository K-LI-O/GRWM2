package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamCategoryDto {
// {Long categoryId, String categoryName, String color
    private Long categoryId;
    private String categoryName;
    private String color;

}
