package GRWM.backend.dto.chatroom;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomAnnouncementDto {

    private Long announcementId;
    private String content;
    private LocalDateTime createdAt;
    private String writerChatName;
}
