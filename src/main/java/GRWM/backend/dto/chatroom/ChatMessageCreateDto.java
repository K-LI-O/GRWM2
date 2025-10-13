package GRWM.backend.dto.chatroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ChatMessageCreateDto {

    private Long chatRoomId;
    private String content;
    private Long replyMessageId;
    private Long communityId; // userId 넘겨주심 됩니도.
}
