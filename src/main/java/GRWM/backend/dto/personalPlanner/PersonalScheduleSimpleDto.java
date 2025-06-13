package GRWM.backend.dto.personalPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 attribute 의 값을 설정하는 생성자
public class PersonalScheduleSimpleDto {

    private Long scheduleId;

    private String title;

    private String categoryName; //카테고리명 반환; 없으면 null
    private String categoryColor; // 카테고리 색상 반환; 없으면 null

    private LocalDateTime startDateTime;

    private LocalDateTime finishDateTime;

}


