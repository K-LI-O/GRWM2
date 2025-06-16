package GRWM.backend.controller;


import GRWM.backend.dto.ChatRoomCreateRequestDto;
import GRWM.backend.dto.ChatRoomEditDto;
import GRWM.backend.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /* POST
    함수명 : createChatRoom
    기능 : 채팅방 생성
    매개변수 : ChatRoomCreateRequestDto;
    반환값 : 채팅방 id
     */

    @PostMapping("/create")
    public Long createChatRoom(@RequestBody ChatRoomCreateRequestDto dto){

        return chatRoomService.createChatRoom(dto);
    }



    /* GET, PATCH
    함수명 : editChatRoom
    기능 : 채팅방 이름 수정 시 기존 이름 및 description 가져오기
    매개변수 : LongChatRoomId
    반환값 : dto
     */

    @GetMapping("/{chatRoomId}/edit")
    public ChatRoomEditDto editChatRoom(@PathVariable Long chatRoomId){
        return chatRoomService.getChatRoomNameAndDescription(chatRoomId);
    }


    @PatchMapping("/{chatRoomId}/edit")
    public ResponseEntity<Void> updateChatRoom(@PathVariable Long chatRoomId, @RequestBody ChatRoomEditDto dto){

        try {
            chatRoomService.updateChatRoom(chatRoomId, dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // 자원을 찾을 수 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 기타 서버 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().build();
        }

    }


    /* DELETE
    함수명 : deleteChatRoom
    기능 : 채팅방 삭제
    매개변수 : Long ChatRoomId
    반환값 : ResponseEntity<Void>
     */

    @DeleteMapping("/{chatRoomId}/delete")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long chatRoomId){

        try {
            chatRoomService.deleteChatRoom(chatRoomId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // 자원을 찾을 수 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 기타 서버 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().build();
        }

    }



}
