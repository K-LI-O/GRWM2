package GRWM.backend.controller;

import GRWM.backend.dto.ChatMessageDto;
import GRWM.backend.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
