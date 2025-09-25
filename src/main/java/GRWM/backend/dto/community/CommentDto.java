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

    private CommunityUserBriefDto userInfo;

    private String content;

    private boolean isPrivate;

    private boolean isEdited;

    // Nullable
    private Long rootCommentId;
}
