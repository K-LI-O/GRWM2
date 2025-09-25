package GRWM.backend.controller.chatroom;

import GRWM.backend.dto.ChatMessageDto;
import GRWM.backend.service.chatroom.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chatroom/message/")
@RequiredArgsConstructor
public class ChatRoomMessageRestController {

    private final ChatMessageService chatMessageService;


    /**
     * function name : showMessageList
     * functionality : 하나의 채팅방에 있는 저장된 메시지 목록을 불러온다.
     * param : chatRoomId
     * return value : dtoList
     */

    @GetMapping("showlist/{chatRoomId}")
    public List<ChatMessageDto> showMessageList(@PathVariable Long chatRoomId){

        return chatMessageService.showMessageList(chatRoomId);
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





}
