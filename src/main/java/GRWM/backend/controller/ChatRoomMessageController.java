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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;


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



    @MessageMapping("/chat/{chatRoomId}/sendMessage") // Client sends to /app/chat/{roomId}/sendMessage
    @SendTo("/topic/chat/{chatRoomId}")              // Server broadcasts to /topic/chat/{roomId}
    // @PreAuthorize("isAuthenticated()")
    public ChatMessageDto sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageCreateDto dto) {
        // 메시지 처리 로직 (DB 저장 등)
        return chatMessageService.saveMessage(chatRoomId, dto);

    }



    /**
     * function name : deleteMessage
     * functionality : 사용자의 메시지를 삭제한다.
     * param :
     * return value :
     */

    @DeleteMapping("/api/chatroom/message/delete")
    public void deleteMessage(Long messageId, Principal principal) {
        // principal의 이름 불러오기
        String memberName = principal.getName();


        // 메시지 처리 로직 (DB 저장 등)


    }




    /**
     * function name : replyMessage
     * functionality : 사용자의 메시지에 답장을 보낸다.
     * param :
     * return value :
     */



}
