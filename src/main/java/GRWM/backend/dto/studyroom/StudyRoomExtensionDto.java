package GRWM.backend.dto.studyroom;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomExtensionDto {
    LocalTime extendedEndTime; //  연장된 종료 시간
    int extendedCount; //  지금까지 연장한 횟수
}
