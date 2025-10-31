package GRWM.backend.dto.studyroom;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactionDto {
    Long reactionId;
    Long todoId;
    Long creatorId;
}
