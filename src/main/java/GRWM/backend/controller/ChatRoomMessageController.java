package GRWM.backend.controller;


import GRWM.backend.dto.ChatMessageCreateDto;
import GRWM.backend.dto.ChatMessageDto;
import GRWM.backend.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatRoomMessageController {

    private final ChatMessageService chatMessageService;


    /**
     * function name : addUserMessage
     * functionality : 사용자가 채팅방에 입장하면 메시지를 보낸다. 이벤트 리스너로 구독 이벤트 처리 가능.
     * param :
     * return value :
     */


//    @MessageMapping("/chat/{roomId}/addUser") // Client sends to /app/chat/{roomId}/sendMessage
//    @SendTo("/topic/chat/{roomId}")              // Server broadcasts to /topic/chat/{roomId}
//    public ChatMessageDto addUserMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageCreateDto dto) {
//        // 메시지 처리 로직 (DB 저장 등)
//        return chatMessageService.saveMessage(chatRoomId, dto);
//
//    }




    /**
     * function name : sendMessage
     * functionality : 일반 메시지를 전송한다
     * param :
     * return value :
     */



    @MessageMapping("/chat/{chatRoomId}/sendMessage") // Client sends to /app/chat/{roomId}/sendMessage
    @SendTo("/topic/chat/{chatRoomId}")              // Server broadcasts to /topic/chat/{roomId}
    @PreAuthorize("isAuthenticated()")
    public ChatMessageDto sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageCreateDto dto) {
        // 메시지 처리 로직 (DB 저장 등)
        return chatMessageService.saveMessage(chatRoomId, dto);

    }



//    /**
//     * function name : leaveUserMessage
//     * functionality : 사용자가 채팅방에서 나가면 메시지를 보낸다.
//     * param :
//     * return value :
//     */
//
//    @MessageMapping("/chat/{chatRoomId}/leaveUser") // Client sends to /app/chat/{roomId}/sendMessage
//    @SendTo("/topic/chat/{chatRoomId}")             // Server broadcasts to /topic/chat/{roomId}
//    public ChatMessageDto leaveUserMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageCreateDto dto) {
//        // 메시지 처리 로직 (DB 저장 등)
//        return chatMessageService.saveMessage(chatRoomId, dto);
//
//    }



    /**
     * function name : replyMessage
     * functionality : 사용자의 메시지에 답장을 보낸다.
     * param :
     * return value :
     */



}
