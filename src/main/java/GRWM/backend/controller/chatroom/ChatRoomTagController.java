package GRWM.backend.controller.chatroom;

import GRWM.backend.dto.ChatRoomTagCreateDto;
import GRWM.backend.service.chatroom.ChatRoomTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-room/category")
@RequiredArgsConstructor

public class ChatRoomTagController {


    private final ChatRoomTagService chatRoomTagService;

    /*
    함수명 : createChatRoomTags
    기능 : 카테고리 생성; 아마 맨 처음에
    param :
        String categoryName
    return value : Long tagId
     */

    @PostMapping("/create")
    public Long createChatRoom(@RequestBody ChatRoomTagCreateDto dto) throws Exception {
        return chatRoomTagService.createChatRoomTags(dto.getTagName());
    }


     /*
    함수명 : deleteChatRoomTags
    기능 : 카테고리 삭제
    param :
        Long tagId
    return value : status 403

     */

    @DeleteMapping("/delete/{tagId}")
    public ResponseEntity<Void> deleteChatRoomTags(@PathVariable Long tagId) throws Exception {
        chatRoomTagService.deleteChatRoomTags(tagId);
        return ResponseEntity.noContent().build();
    }


    /* PATCH
    함수명 : updateChatRoomTags
    기능 : 카테고리 수정
    param : Dto
        String categoryName
    return value : status

     */


    @GetMapping("/edit/{tagId}")
    public ChatRoomTagCreateDto updateChatRoomTags(@PathVariable Long tagId) throws Exception{
        return chatRoomTagService.findOneTag(tagId);
    }


    @PatchMapping("/edit/{tagId}")
    public ResponseEntity<Void> updateChatRoomTags(@PathVariable Long tagId, @RequestBody ChatRoomTagCreateDto dto) throws Exception{
        chatRoomTagService.updateChatRoomTag(tagId, dto);

        return ResponseEntity.ok().build();
    }



    /* get
    함수명 : findChatRoomTags
    기능 : 이미 존재하는 카테고리 목록 조회
    param : Dto
        Long tagId
        String categoryName
    return value : List<dto>

     */

    @GetMapping("/list")
    public List<ChatRoomTagCreateDto> findChatRoomTags(){
        return chatRoomTagService.findAllChatRoomTags();
    }


}
