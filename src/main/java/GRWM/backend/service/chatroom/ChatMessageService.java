package GRWM.backend.service.chatroom;

import GRWM.backend.dto.ChatMessageCreateDto;
import GRWM.backend.dto.ChatMessageDto;
import GRWM.backend.entity.chatroom.ChatMessage;
import GRWM.backend.entity.chatroom.ChatRoom;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.chatroom.ChatMessageRepository;
//import GRWM.backend.repository.chatroom.ChatRoomMemberRepository;
import GRWM.backend.repository.chatroom.ChatRoomCommunityRepository;
import GRWM.backend.repository.chatroom.ChatRoomRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {


    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    //private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomCommunityRepository ccRepository;
    private final CommunityUserRepository communityUserRepository;

    /*
    함수명 : saveMessage
    기능 : 메시지를 DB 에 저장하고 리턴한다.
    매개변수 : Long chatRoomId, chatMessageCreateDTO(채팅방 아이디, 내용, 커뮤니티 아이디)
    반환값 : Long messageId
    warning : 이것은... 기존 멤버의 일반적인 chat 타입 메시지를 저장하는 함수이다.

     */

    @Transactional
    public ChatMessageDto saveMessage(Long chatRoomId, ChatMessageCreateDto dto, Long communityId){

        // 채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);

        // 멤버 아이디 가져오기
        String nickname = extractOptionalUser(dto.getCommunityId()).getNickname();
        System.out.println("get the member ID\n");

        // 메시지 객체 생성
        ChatMessage message = new ChatMessage(
                dto.getCommunityId(),
                dto.getContent(),
                ChatMessage.MessageType.CHAT.ordinal(),
                chatRoom,
                nickname
        );
        ChatMessage savedMessage = messageRepository.save(message);

        // 전송할 dto 객체 생성
        ChatMessageDto newDto = new ChatMessageDto(
                savedMessage.getId(),
                savedMessage.getMemberId(), // 멤버 아이디라고 되어있지만 커뮤니티 아이디임
                savedMessage.getType().ordinal(),
                savedMessage.getContent(),
                savedMessage.getCreatedAt(),
                savedMessage.getWriterChatName()

        );
        System.out.println("save the message");
        return newDto;
    }


    /*
    함수명 : showMessageList
    기능 : DB 에 저장된 메시지 리스트를 조회하여 반환한다.
    매개변수 : Long chatRoomId,
    반환값 : chatMessageDTO


     */

    public List<ChatMessageDto> showMessageList(Long chatRoomId){

        List<ChatMessage> messageList = messageRepository.findByChatRoom_IdOrderByCreatedAtDesc(chatRoomId);
        List<ChatMessageDto> dtoList = new ArrayList<>();

        for(ChatMessage chatMessage : messageList){
            ChatMessageDto dto = new ChatMessageDto(chatMessage.getId(),
                    chatMessage.getMemberId(),
                    chatMessage.getType().ordinal(),
                    chatMessage.getContent(),
                    chatMessage.getCreatedAt(),
                    chatMessage.getWriterChatName());

            dtoList.add(dto);

        }

        return dtoList;
    }

    // DeleteMessage

    public void deleteMessage(Long messageId, Long communityId){
        // 원하는 메시지 불러오기
        ChatMessage message = messageRepository.findByIdAndMemberId(messageId, communityId);
        if(message != null){
            messageRepository.delete(message);
        } else{
            throw new RuntimeException("존재하지 않는 메시지입니다.");
        }

    }



    private CommunityUser extractOptionalUser(Long communityId){
        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);

        if (communityId == null) {
            // 필수 ID가 누락되었음을 알리는 명확한 예외 발생
            // (NullPointerException 방지 및 오류 맥락 제공)
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }








}
