package GRWM.backend.dto.personalPlanner;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDto {

    private Long categoryId;

    private String categoryName;

    private String color;
}
