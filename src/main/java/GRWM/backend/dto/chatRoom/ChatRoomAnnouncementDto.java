package GRWM.backend.dto.chatRoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChatRoomAnnouncementDto {

    private Long announcementId;
    private String content;
    private LocalDateTime createdAt;
    private String writerChatName;
}
