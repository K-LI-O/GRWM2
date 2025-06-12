package GRWM.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 attribute 의 값을 설정하는 생성자
public class PersonalScheduleDto {

    private Long scheduleId;


    private String title;

    private String category;

    private LocalDateTime startDateTime;

    private LocalDateTime finishDateTime;

    private String location;

    private String memo;

}
