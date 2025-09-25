package GRWM.backend.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateDto {

    // 생성된 댓글을 response 하는 DTO 이다.

    private Long commentId;

    private LocalDateTime createdAt;

    private String content;

    private boolean isPrivate;

    // Nullable
    private Long rootCommentId;

}
