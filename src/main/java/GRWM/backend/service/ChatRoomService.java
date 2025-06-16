package GRWM.backend.service;


import GRWM.backend.dto.ChatRoomCreateRequestDto;
import GRWM.backend.dto.ChatRoomEditDto;
import GRWM.backend.entity.ChatRoom;
import GRWM.backend.entity.ChatRoomMember;
import GRWM.backend.entity.Member;
import GRWM.backend.repository.ChatRoomMemberRepository;
import GRWM.backend.repository.ChatRoomRepository;
import GRWM.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    /* POST
    함수명 : createChatRoom
    기능 : 채팅방 생성
    매개변수 : ChatRoomCreateRequestDto;
    반환값 : 채팅방 id
     */

    @Transactional
    public Long createChatRoom(ChatRoomCreateRequestDto dto){

        Member member = memberRepository.getReferenceById(dto.getUserId());

        // dto.maxMembers 설정
        if(dto.getMaxMembers() == 0){
         dto.setMaxMembers(30);
        }

        // Boolean wrapper 풀기
        boolean actualIsPrivate = dto.getIsPrivate() != null ? dto.getIsPrivate() : false;

        // 채팅방 객체 생성
        ChatRoom chatRoom = new ChatRoom(dto.getRoomName(), dto.getDescription(), actualIsPrivate, dto.getPassword(), dto.getMaxMembers(), member);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return savedChatRoom.getId();
    }



    /* PUT
    함수명 : getChatRoomNameAndDescription
    기능 : 채팅방 이름 수정 시 기존 이름 및 description 가져오기
    매개변수 : LongChatRoomId
    반환값 : dto
     */

    @Transactional(readOnly = true)
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


    @Transactional
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

    @Transactional
    public void deleteChatRoom(Long chatRoomId){
        chatRoomRepository.deleteById(chatRoomId);
    }



    /* POST
    함수명 : verifyChatRoomPassword
    기능 : 멤버의 채팅방 입장 이전 비밀번호가 있는 경우 검증 로직
    매개 변수 : requestBody String password
    반환값 : ResponseEntity<Void> 200 / 403
     */

    @Transactional(readOnly = true)
    public boolean verifyChatRoomPassword(Long chatRoomId, String password){

        ChatRoom chatRoom;
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);

        // 채팅방 비밀번호 가져오기
        String originalPassword;
        if(optionalChatRoom.isPresent()){
            chatRoom = optionalChatRoom.get();
            originalPassword = chatRoom.getPassword();

        } else {
            // 플래너를 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Planner not found with ID: " + chatRoomId);
        }

        // 비밀번호 검증 후 boolean 반환
        if(password.equals(originalPassword)){
            return true;

        } else{
            return false;
        }

    }




        /* POST
    함수명 : JoinChatRoom
    기능 : 멤버가 채팅방에 입장
    매개 변수 : path variable userId, path variable chatRoomId,
    반환값 : ResponseEntity<Void>
     */

    @Transactional
    public void joinChatRoom(Long memberId, Long chatRoomId, String chatName){

        // 멤버 객체 불러오기
        Member member;
        try {
            member = memberRepository.getReferenceById(memberId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 채팅방 객체 불러오기
        ChatRoom chatRoom;
        try{
            chatRoom = chatRoomRepository.getReferenceById(chatRoomId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


       // 채팅방-멤버 객체 생성하기
        ChatRoomMember joinInfo = new ChatRoomMember(chatRoom, member, chatName);
        // 그리고 저장하기.
        chatRoomMemberRepository.save(joinInfo);

       // 끗.
    }



}
