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
public class PersonalScheduleDto {

    private Long scheduleId;

    private String title;

    private CategoryInfoDto category; //카테고리명 반환

    private LocalDateTime startDateTime;

    private LocalDateTime finishDateTime;

    private String location;

    private String memo;

}


