package GRWM.backend.dto.teamPlanner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.repository.query.Param;

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
