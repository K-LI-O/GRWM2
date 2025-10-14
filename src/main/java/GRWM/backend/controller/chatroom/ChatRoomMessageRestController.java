package GRWM.backend.controller.chatroom;

import GRWM.backend.dto.chatroom.ChatMessageDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.chatroom.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatRoomMessageRestController {

    private final ChatMessageService chatMessageService;


    /**
     * function name : showMessageList
     * functionality : 하나의 채팅방에 있는 저장된 메시지 목록을 불러온다.
     * param : chatRoomId
     * return value : dtoList
     */

    @GetMapping("/{chatRoomId}/show")
    public List<ChatMessageDto> showMessageList(@PathVariable Long chatRoomId){

        return chatMessageService.showMessageList(chatRoomId);
    }

    /**
     * function name : deleteMessage
     * functionality : 사용자의 메시지를 삭제한다.
     * param :
     * return value :
     */

    @DeleteMapping("/{chatRoomId}/delete/{messageId}")
    public void deleteMessage(@PathVariable Long messageId,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {


        //chatMessageService.deleteMessage(messageId, userDetails.getCommunityUserId());
        // 메시지 처리 로직 (DB 저장 등)


    }





}
