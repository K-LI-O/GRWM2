package GRWM.backend.dto.studyroom;

import lombok.*;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtensionDto {
    private LocalTime extendedEndTime; // 연장된 종료 시간
    private int extendedCount; // 지금까지 연장한 횟수

}
