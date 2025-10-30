package GRWM.backend.dto.studyroom;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomTodoCreateDto {
    private String title;
    private String description;


}
