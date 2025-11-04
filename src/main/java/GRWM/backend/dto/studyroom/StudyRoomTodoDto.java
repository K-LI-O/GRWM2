package GRWM.backend.dto.studyroom;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomTodoDto {
    Long todoId;
    Long creatorId; // (사용자의 커뮤니티 아이디)
    String content;
    boolean isCompleted;
    List<String> reactions;
    String type;

}
