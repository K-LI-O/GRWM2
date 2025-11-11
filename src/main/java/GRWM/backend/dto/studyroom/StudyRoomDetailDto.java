package GRWM.backend.dto.studyroom;

import lombok.*;

import java.time.LocalTime;
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomDetailDto {
    StudyRoomDto studyRoom;
    String currentUserStatus; // "joined" | "owner";


}
