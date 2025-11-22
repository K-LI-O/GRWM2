package GRWM.backend.dto.personalPlanner;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryInfoDto {

    private Long categoryId;


    private String categoryName;

    private String color;
}
