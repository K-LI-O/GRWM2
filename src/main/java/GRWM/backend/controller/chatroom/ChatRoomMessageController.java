package GRWM.backend.controller.chatroom;


import GRWM.backend.dto.chatroom.ChatMessageCreateDto;
import GRWM.backend.dto.chatroom.ChatMessageDeleteDto;
import GRWM.backend.dto.chatroom.ChatMessageDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.chatroom.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatRoomMessageController {

    private final ChatMessageService chatMessageService;




    /**
     * function name : sendMessage
     * functionality : 일반 메시지를 전송한다
     * param :
     * return value :
     */



    @MessageMapping("/chat.{chatRoomId}.sendMessage") // Client sends to /app/chat/{roomId}/sendMessage
    @SendTo("/topic/chat.{chatRoomId}")              // Server broadcasts to /topic/chat/{roomId}
    // @PreAuthorize("isAuthenticated()")
    public ChatMessageDto sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageCreateDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 메시지 처리 로직 (DB 저장 등)
        return chatMessageService.saveMessage(chatRoomId, dto, userDetails.getCommunityUserId());

    }




    /**
     * function name : deleteMessage
     * functionality : 사용자의 메시지 삭제.
     * param :
     * return value :
     */

    @MessageMapping("/chat.{chatRoomId}.deleteMessage") // Client sends to /app/chat/{roomId}/sendMessage
    @SendTo("/topic/chat.{chatRoomId}")              // Server broadcasts to /topic/chat/{roomId}
    // @PreAuthorize("isAuthenticated()")
    public ChatMessageDeleteDto deleteMessage(@DestinationVariable Long chatRoomId,
                                              @Payload ChatMessageDeleteDto dto,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 메시지 처리 로직 (DB 저장 등)
        return chatMessageService.deleteMessage(chatRoomId,dto.getDeleteMessageId(), userDetails.getCommunityUserId());

    }




}
