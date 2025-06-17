package GRWM.backend.controller;


import GRWM.backend.dto.*;
import GRWM.backend.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    /*
    함수명 : showChatRoomInfo
    기능 : 특정 채팅방 하나의 정보를 전달한다
    매개변수 : path variable chatRoomId
    반환값 : Dto; 채팅방명, description, isPrivate, 현재 입장한 사람들;
     */

    @GetMapping("/show/{chatRoomId}")
    public ChatRoomShowDto showChatRoomInfo(@PathVariable Long chatRoomId){
        return chatRoomService.showChatRoomInfo(chatRoomId);
    }



    /*
    함수명 : showJoinedChatRoomListInfo
    기능 : 사용자가 입장해 있는 채팅방들의 정보를 전달한다
    매개변수 : path var Long userId
    반환값 : Dto; 채팅방명, description, isPrivate, 최대 인원, 현재 입장한 사람들;
     */

    @GetMapping("/show/{userId}/joinlist")
    public List<ChatRoomShowDto> showJoinedChatRoomListInfo(@PathVariable Long userId){
        return chatRoomService.showJoinedChatRoomListDto(userId);

    }



    /*
    함수명 : 채팅방 검색
    기능 : 키워드로 채팅방을 검색한다.
    매개변수 : path variable String tag
    반환값 : Dto; String chatRoomName, String description, Bool isPrivate, int maxMembers int currentMembers;
//     */
//
//    @GetMapping("search/{tag}")
//    public List<ChatRoomShowDto> searchChatRoomListByTag(@PathVariable String tag){
//
//    }


    /*
    함수명 : createAnnouncement
    기능 : 채팅방 공지 저장하고 공지사항의 아이디 리턴
    매개변수 :
    path variable chatRoomId,
    requestBody 공지 dto : Long userId(작성자), String content, Bool isMain();

    반환값 : Dto - Long id, String content, LocalDateTime createdAt, String writerChatName;
     */

    @PostMapping("/{chatRoomId}/announcement/create")
    public Long createAnnouncement(@PathVariable Long chatRoomId,
                                                      @RequestBody ChatRoomAnnouncementCreateDto dto){

        return chatRoomService.createAnnouncement(chatRoomId, dto);
    }

    /*
    함수명 : createAnnouncement
    기능 : 채팅방 메인 공지 반환
    매개변수 :
    path variable chatRoomId,
    requestBody 공지 dto : Long userId(작성자), String content, Bool isMain();

    반환값 : Dto - Long id, String content, Bool isMain, LocalDateTime createdAt, String writerChatName;
     */


    @GetMapping("/{chatRoomId}/announcement/show/main")
    public ChatRoomAnnouncementDto showMainAnnouncement(@PathVariable Long chatRoomId){

        return chatRoomService.showMainAnnouncement(chatRoomId);

    }





}
