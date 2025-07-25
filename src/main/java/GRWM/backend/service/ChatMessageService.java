package GRWM.backend.service;

import GRWM.backend.dto.ChatMessageCreateDto;
import GRWM.backend.dto.ChatMessageDto;
import GRWM.backend.dto.chatRoom.ChatRoomJoinDto;
import GRWM.backend.entity.ChatMessage;
import GRWM.backend.entity.ChatRoom;
import GRWM.backend.entity.ChatRoomMember;
import GRWM.backend.repository.ChatMessageRepository;
import GRWM.backend.repository.ChatRoomMemberRepository;
import GRWM.backend.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {


    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    /**
     * name : save
     * functionality : 사용자 입장 메시지를 저장한다.
     * param :
     * return :
     */

    public ChatMessageDto saveJoinMessage(Long chatRoomId, ChatRoomJoinDto dto){

        // 채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);

        // 메시지 객체 생성
        ChatMessage message = new ChatMessage(
                dto.getChatName()+ "님이 입장하셨습니다.",
                ChatMessage.MessageType.JOIN.ordinal(),
                chatRoom,
                dto.getChatName()
        );
        ChatMessage savedMessage = messageRepository.save(message);

        ChatMessageDto newDto = new ChatMessageDto(
                savedMessage.getId(),
                savedMessage.getType().ordinal(),
                savedMessage.getContent(),
                savedMessage.getCreatedAt(),
                savedMessage.getWriterChatName()
        );

        return newDto;

    }



    /**
     * name :
     * functionality : 사용자 퇴장 메시지를 저장한다.
     * @param chatRoomId
     * @param userId
     */

    public ChatMessageDto saveLeaveMessage(Long userId, Long chatRoomId){

        // 채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);

        // 채팅방 사용자 가져오기

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByMember_IdAndChatRoom_Id(userId, chatRoomId);

        // 메시지 객체 생성
        ChatMessage message = new ChatMessage(
                 chatRoomMember.getChatName()+ "님께서 퇴장하셨습니다. ",
                ChatMessage.MessageType.LEAVE.ordinal(),
                chatRoom,
                chatRoomMember.getChatName()
        );
        ChatMessage savedMessage = messageRepository.save(message);

        ChatMessageDto newDto = new ChatMessageDto(
                savedMessage.getId(),
                savedMessage.getType().ordinal(),
                savedMessage.getContent(),
                savedMessage.getCreatedAt(),
                savedMessage.getWriterChatName()
        );

        return newDto;

    }

    /*
    함수명 : saveMessage
    기능 : 메시지를 DB 에 저장하고 리턴한다.
    매개변수 : Long chatRoomId, chatMessageCreateDTO
    반환값 : Long messageId
    warning : 이것은... 기존 멤버의 일반적인 chat 타입 메시지를 저장하는 함수이다.

     */

    public ChatMessageDto saveMessage(Long chatRoomId, ChatMessageCreateDto dto){

        // 채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);

        // 메시지 객체 생성
        ChatMessage message = new ChatMessage(
                dto.getContent(),
                ChatMessage.MessageType.CHAT.ordinal(),
                chatRoom,
                dto.getWriterChatName()
        );
        ChatMessage savedMessage = messageRepository.save(message);

        // 전송할 dto 객체 생성
        ChatMessageDto newDto = new ChatMessageDto(
                savedMessage.getId(),
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
    기능 : 메시지 리스트를 DB 에 저장하고 리턴한다.
    매개변수 : Long chatRoomId,
    반환값 : chatMessageDTO


     */

    public List<ChatMessageDto> showMessageList(Long chatRoomId){

        List<ChatMessage> messageList = messageRepository.findByChatRoom_IdOrderByCreatedAtDesc(chatRoomId);
        List<ChatMessageDto> dtoList = new ArrayList<>();

        for(ChatMessage chatMessage : messageList){
            ChatMessageDto dto = new ChatMessageDto(chatMessage.getId(),
                    chatMessage.getType().ordinal(),
                    chatMessage.getContent(),
                    chatMessage.getCreatedAt(),
                    chatMessage.getWriterChatName());

            dtoList.add(dto);

        }

        return dtoList;
    }











}
