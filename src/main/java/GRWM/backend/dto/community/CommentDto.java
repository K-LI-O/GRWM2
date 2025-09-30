package GRWM.backend.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long commentId;

    private CommunityUserBriefDto User;

    private String content;

    private boolean isPrivate;

    // Nullable
    private Long parentCommentId;

    private boolean isEdited;

}
