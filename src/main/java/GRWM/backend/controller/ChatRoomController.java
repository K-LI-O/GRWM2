package GRWM.backend.controller;


import GRWM.backend.dto.ChatRoomCreateRequestDto;
import GRWM.backend.dto.ChatRoomEditDto;
import GRWM.backend.dto.ChatRoomPasswordDto;
import GRWM.backend.dto.personalPlanner.ChatRoomJoinDto;
import GRWM.backend.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    /* POST
    함수명 : verifyChatRoomPassword
    기능 : 멤버의 채팅방 입장 이전 비밀번호가 있는 경우 검증 로직
    매개 변수 : requestBody String password
    반환값 : ResponseEntity<Void> 200 / 403
     */

    @PostMapping("/{chatRoomId}/verify")
    public ResponseEntity<Void> verifyChatRoomPassword(@PathVariable Long chatRoomId, @RequestBody ChatRoomPasswordDto dto){
        boolean isRight = chatRoomService.verifyChatRoomPassword(chatRoomId, dto.getPassword());

        if(isRight) {
            return ResponseEntity.ok().build();
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // if-else로 구현하였으나 추후 try-catch 문으로 수정할 것;

    }




    /* POST
    함수명 : JoinChatRoom
    기능 : 멤버가 채팅방에 입장
    매개 변수 : path variable userId, path variable chatRoomId, String chatName(단일 스트링)
    반환값 : ResponseEntity<Void>
     */

    @PostMapping("/{chatRoomId}/join")
    public ResponseEntity<Void> joinChatRoom(@PathVariable Long chatRoomId,
                                             @RequestBody ChatRoomJoinDto dto){

        try{
            chatRoomService.joinChatRoom(dto.getUserId(), chatRoomId, dto.getChatName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }








}
