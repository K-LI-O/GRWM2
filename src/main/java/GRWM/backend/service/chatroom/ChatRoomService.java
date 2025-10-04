package GRWM.backend.service.chatroom;


import GRWM.backend.dto.*;
import GRWM.backend.entity.chatroom.*;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.chatroom.*;
import GRWM.backend.repository.user.CommunityUserRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    //private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomAnnouncementRepository announcementRepository;
    private final ChatRoomTagRepository chatRoomTagRepository;

    private final CommunityUserRepository communityUserRepository;
    private final ChatRoomCommunityRepository chatRoomCommunityRepository;


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

        // 채팅방 카테고리 객체 불러오기
        Optional<ChatRoomTag> optionalTag = chatRoomTagRepository.findById(dto.getCategory());
        ChatRoomTag tag = null;

        try {
            if (optionalTag.isPresent()) {
                tag = optionalTag.get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        // 채팅방 객체 생성
        ChatRoom chatRoom = new ChatRoom(
                dto.getRoomName(), dto.getDescription(), tag, actualIsPrivate, dto.getPassword(), dto.getMaxMembers(), member);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // dto 생성;
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
    public void joinChatRoom(Long communityId, Long chatRoomId, ChatRoomJoinDto dto){

        // (커뮤니티)멤버 객체 불러오기
        CommunityUser cu;
        try {
            cu = communityUserRepository.getReferenceById(communityId);
        } catch (Exception e) {
            throw new RuntimeException("존재하지 않는 커뮤니티 프로필입니다.");
        }

        // 채팅방 객체 불러오기
        ChatRoom chatRoom;
        try{
            chatRoom = chatRoomRepository.getReferenceById(chatRoomId);
            int currentNumber = chatRoom.getCurrentMembers();
            chatRoom.setCurrentMembers(currentNumber + 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


       // 채팅방-커뮤니티유저 객체 생성하기
        ChatRoomCommunity joinInfo = new ChatRoomCommunity(chatRoom, cu, dto.getIsManager());
        // 그리고 저장하기.
        chatRoomCommunityRepository.save(joinInfo);

       // 끗.
    }



    /* POST
    함수명 : leaveChatRoom
    기능 : 멤버가 채팅방에서 퇴장
    매개 변수 : Long userId, Long chatRoomId,
    반환값 : void
     */
    @Transactional
    public void leaveChatRoom(Long communityId, Long chatRoomId){
        // chatroomMemberId 구하기
        //ChatRoomMember cm = chatRoomMemberRepository.findByMember_IdAndChatRoom_Id(userId, chatRoomId);
        ChatRoomCommunity cc = chatRoomCommunityRepository.findByCommunityUserAndChatRoom(
                extractOptionalUser(communityId),
                extractOptionalChatroom(chatRoomId)
        );
        // chatroomMember 객체 삭제
        chatRoomCommunityRepository.delete(cc);

    }




    /*
    함수명 : showChatRoomInfo
    기능 : 특정 채팅방 하나의 정보를 전달한다
    매개변수 : path variable chatRoomId
    반환값 : Dto; 채팅방명, description, isPrivate, 현재 입장한 사람들;
     */
    @Transactional(readOnly = true)
    public ChatRoomShowDto showChatRoomInfo(Long chatRoomId, Long communityId){

        // 채팅방 객체 가져오기
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);
        ChatRoom chatRoom;

        if(optionalChatRoom.isPresent()){
            chatRoom = optionalChatRoom.get();
        } else {
            // 플래너를 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Planner not found with ID: " + chatRoomId);
        }
        // 카테고리 객체를 아이디로 바꾸기.
        String tagContent = chatRoom.getChatRoomTag().getContent();

        // dto에 정보 싣기
        ChatRoomShowDto dto = new ChatRoomShowDto(
                chatRoom.getId(),
                chatRoom.getName(),
                chatRoom.getDescription(),
                tagContent, chatRoom.isPrivate(),
                chatRoom.getMaxMembers(), chatRoom.getCurrentMembers(),
                getIsManager(communityId, chatRoomId)
                );

        return dto;
    }


    /*
    함수명 : showJoinedChatRoomListInfo
    기능 : 사용자가 입장해 있는 채팅방들의 정보를 전달한다
    매개변수 : path var Long userId
    반환값 : Dto list; 채팅방명, description, isPrivate, 최대 인원, 현재 입장한 사람들;
     */
    @Transactional(readOnly = true)
    public List<ChatRoomShowDto> showJoinedChatRoomListDto(Long communityId){

        // 유저 아이디로 채팅방과 멤버의 중간 테이블 리스트 찾아오기
        // List<ChatRoomMember> joinedChatRoomMemberList = chatRoomMemberRepository.findByMember_Id(userId);

        List<ChatRoomCommunity> joinedChatRoomMemberList = chatRoomCommunityRepository.findByCommunityUser(
                extractOptionalUser(communityId)
        );

        // 채팅방 객체 찾아오기
        List<ChatRoom> joinedChatRoomList = joinedChatRoomMemberList.stream()
                .map(ChatRoomCommunity::getChatRoom) // getChatRoom()을 통해 연관된 ChatRoom 객체 로드
                .collect(Collectors.toList());

        // 채팅방 객체 dto로 변환

        List<ChatRoomShowDto> dtoList = new ArrayList<>();
        for(ChatRoom chatRoom : joinedChatRoomList){

            ChatRoomShowDto dto = new ChatRoomShowDto(
                    chatRoom.getId(),
                    chatRoom.getName(), chatRoom.getDescription(),
                    chatRoom.getChatRoomTag().getContent(), chatRoom.isPrivate(), chatRoom.getMaxMembers(),
                    chatRoom.getCurrentMembers(),
                    getIsManager(communityId, chatRoom.getId()));

            dtoList.add(dto);
        }

        // 반환
        return dtoList;
    }



            /*
    함수명 : showChatRoomListInfo
    기능 : 모든 채팅방들의 정보를 전달한다
    매개변수 : xx
    반환값 : Dto; 채팅방명, description, isPrivate, 최대 인원, 현재 입장한 사람들;
     */
    @Transactional(readOnly = true)
    public List<ChatRoomShowDto> showChatRoomListInfo(Long communityId){
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();

        List<ChatRoomShowDto> dtoList = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList){

            ChatRoomShowDto dto = new ChatRoomShowDto(
                    chatRoom.getId(),
                    chatRoom.getName(), chatRoom.getDescription(),
                    chatRoom.getChatRoomTag().getContent(), chatRoom.isPrivate(), chatRoom.getMaxMembers(),
                    chatRoom.getCurrentMembers(),
                    getIsManager(communityId, chatRoom.getId()));

            dtoList.add(dto);
        }
        return dtoList;
    }



    /*
    함수명 : searchChatRoomListByTag
    기능 : 키워드로 채팅방을 검색한다.
    매개변수 : path variable String tag
    반환값 : Dto; String chatRoomName, String description, Bool isPrivate, int maxMembers int currentMembers;
     */

//    public List<ChatRoomAnnouncementDto> searchChatRoomListByTag(String tag){
//
//        //
//    }


        /*
    함수명 : createAnnouncement
    기능 : 채팅방 공지 저장하고 바로 내용 리턴.
    매개변수 :
    path variable chatRoomId,
    requestBody 공지 dto : Long userId(작성자), String content, Bool isMain();

    반환값 : Dto - Long id, String content, Bool isMain, LocalDateTime createdAt, String writerChatName;
     */


    @Transactional
    public Long createAnnouncement(Long chatRoomId,
                                   ChatRoomAnnouncementCreateDto dto){

        // 채팅방 객체 가져오기
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);

        // 채팅방 아이디와 사용자 아이디로 채팅방-멤버 객체 가져오기, 기반으로 멤버 객체 가져오기
        //ChatRoomMember joinedMember = chatRoomMemberRepository.findByMember_IdAndChatRoom_Id(dto.getUserId(), chatRoomId);

        ChatRoomCommunity joinedMember = chatRoomCommunityRepository.findByCommunityUserAndChatRoom(
                extractOptionalUser(dto.getUserId()),
                extractOptionalChatroom(chatRoomId)
        );
        String chatName = joinedMember.getCommunityUser().getNickname();


        // 새로운 announcement 객체 생성
        ChatRoomAnnouncement announcement = new ChatRoomAnnouncement(dto.getContent(), chatRoom, chatName);

        ChatRoomAnnouncement savedAnnouncement = announcementRepository.save(announcement);

        return savedAnnouncement.getId();
    }


    /*
    함수명: getIsManager
    기능: 사용작 해당 채팅방의 방장인지 여부를 반환한다.
    매개변수: Long communityId, Long chatroomId
    반환값: boolean
     */

    private boolean getIsManager(Long communityId, Long chatroomId){
        CommunityUser user = extractOptionalUser(communityId);
        ChatRoom chatroom = extractOptionalChatroom(chatroomId);

        ChatRoomCommunity cc = chatRoomCommunityRepository.findByCommunityUserAndChatRoom(user,chatroom);
        return cc.isManager();
    }






    /*
    함수명 : showMainAnnouncement
    기능 : 채팅방에서 가장 최근에 생성한 공지 반환
    매개변수 :
    path variable Long chatRoomId,

    반환값 : Dto - Long id, String content, LocalDateTime createdAt, String writerChatName;
     */

    @Transactional(readOnly = true)
    public ChatRoomAnnouncementDto showMainAnnouncement(Long chatRoomId){

        // 특정 채팅방에서 가장 최근에 올라간 공지 불러오기
        ChatRoomAnnouncement mainAnnouncement = announcementRepository.findFirstByChatRoom_IdOrderByCreatedAtDesc(chatRoomId);

        if (mainAnnouncement != null) { // Null 체크 추가
            // dto에 담기
            ChatRoomAnnouncementDto dto = new ChatRoomAnnouncementDto(
                    mainAnnouncement.getId(),
                    mainAnnouncement.getContent(),
                    mainAnnouncement.getCreatedAt(),
                    mainAnnouncement.getWriterChatName()
            );

            return dto;
        } else{
            ChatRoomAnnouncementDto dto = null;
            return dto;
        }


    }


    private CommunityUser extractOptionalUser(Long communityId){
        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);
        CommunityUser user = null;

        try {
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 프로필입니다.");
        }

        return user;
    }




    private ChatRoom extractOptionalChatroom(Long chatroomId){
        Optional<ChatRoom> optionalChatroom = chatRoomRepository.findById(chatroomId);
        ChatRoom chatroom = null;

        try {
            if (optionalChatroom.isPresent()) {
                chatroom = optionalChatroom.get();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 채팅방입니다.");
        }

        return chatroom;
    }






}
