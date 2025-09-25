package GRWM.backend.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private Long commentId;

    private Long communityId;

    private String content;

    private LocalDateTime createdAt;

    private boolean isUpdated;

    private boolean isReply;

    // Nullable
    private Long rootCommentId;
}
