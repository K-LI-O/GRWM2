package GRWM.backend.dto.chatroom;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatMessageDeleteDto {
    Long deleteMessageId;
    Long chatRoomId;
    Long senderId;
}
