package GRWM.backend.service;


import GRWM.backend.dto.ChatRoomCreateRequestDto;
import GRWM.backend.dto.ChatRoomEditDto;
import GRWM.backend.entity.ChatRoom;
import GRWM.backend.entity.Member;
import GRWM.backend.repository.ChatRoomRepository;
import GRWM.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    /* POST
    함수명 : createChatRoom
    기능 : 채팅방 생성
    매개변수 : ChatRoomCreateRequestDto;
    반환값 : 채팅방 id
     */

    public Long createChatRoom(ChatRoomCreateRequestDto dto){

        Member member;
        // 사용자 객체 가져오기
        try{
        member = memberRepository.getReferenceById(dto.getUserId());
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        // dto.maxMembers 설정
        if(dto.getMaxMembers() == 0){
         dto.setMaxMembers(30);
        }

        // 채팅방 객체 생성
        ChatRoom chatRoom = new ChatRoom(dto.getRoomName(), dto.getDescription(), dto.isPrivate(), dto.getPassword(), dto.getMaxMembers(), member);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return savedChatRoom.getId();
    }


    /* PUT
    함수명 : getChatRoomNameAndDescription
    기능 : 채팅방 이름 수정 시 기존 이름 및 description 가져오기
    매개변수 : LongChatRoomId
    반환값 : dto
     */

    public ChatRoomEditDto getChatRoomNameAndDescription(Long chatRoomId) {
        // 채팅방 객체 가져오기
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);
        ChatRoom chatRoom;

        if (optionalChatRoom.isPresent()) {
            chatRoom = optionalChatRoom.get();

            ChatRoomEditDto dto = new ChatRoomEditDto(chatRoom.getName(), chatRoom.getDescription());
            return dto;

        } else {
            // 채팅방을 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Planner not found with ID: " + chatRoomId);
        }

    }


    public void updateChatRoom(Long chatRoomId, ChatRoomEditDto dto){
        // 채팅방 객체 가져오기
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);
        ChatRoom chatRoom;

        if (optionalChatRoom.isPresent()) {

            // 채팅방 정보 변경
            chatRoom = optionalChatRoom.get();
            chatRoom.setName(dto.getRoomName());

            // description이 비어있다면 업데이트하지 않는다.
            if(dto.getDescription() != null) {
                chatRoom.setDescription(dto.getDescription());
            }
            // 채팅방 정보 저장
            chatRoomRepository.save(chatRoom);

        } else {
            // 채팅방을 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Planner not found with ID: " + chatRoomId);
        }
    }


    /* DELETE
    함수명 : deleteChatRoom
    기능 : 채팅방 삭제
    매개변수 : Long ChatRoomId
    반환값 : void
     */

    public void deleteChatRoom(Long chatRoomId){
        chatRoomRepository.deleteById(chatRoomId);
    }




}
