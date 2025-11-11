package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamCategoryCreateDto {

    private String name;
    private String color; // (#FFFFFF 형식의 컬러코드를 저장)

}
