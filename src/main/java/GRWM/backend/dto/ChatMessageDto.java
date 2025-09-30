package GRWM.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private Long messageId;
    private Long senderId;
    private int type; //(0 chat, 1 join, 2 leave)
    private String content;
    private LocalDateTime createdAt;
    private String writerChatName;
    // private boolean isMyChat;

}
