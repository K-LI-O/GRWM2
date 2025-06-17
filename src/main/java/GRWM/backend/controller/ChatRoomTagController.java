package GRWM.backend.controller;

import GRWM.backend.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor

public class ChatRoomTagController {


    private final ChatRoomService chatRoomService;

    /*
    함수명 : fillChatRoomTags
    기능 : 카테고리 생성, 만일 이미 카테고리가 존재한다면 카테고리 객체 가져오기
    사용자는 카테고리를 기입한다.
    String tag dt;

     */


}
