package GRWM.backend.dto;

import jakarta.persistence.Column;
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
public class PersonalScheduleCreateRequestDto {

    private Long plannerId;

    private Optional<Long> categoryId;


    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime finishDateTime;

    private String location;

    private String memo;

}
