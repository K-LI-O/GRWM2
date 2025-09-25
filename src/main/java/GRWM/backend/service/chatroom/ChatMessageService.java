package GRWM.backend.service;

import GRWM.backend.dto.ChatMessageCreateDto;
import GRWM.backend.dto.ChatMessageDto;
import GRWM.backend.entity.chatroom.ChatMessage;
import GRWM.backend.entity.chatroom.ChatRoom;
import GRWM.backend.repository.chatroom.ChatMessageRepository;
import GRWM.backend.repository.chatroom.ChatRoomMemberRepository;
import GRWM.backend.repository.chatroom.ChatRoomRepository;
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


    /*
    함수명 : saveMessage
    기능 : 메시지를 DB 에 저장하고 리턴한다.
    매개변수 : Long chatRoomId, chatMessageCreateDTO
    반환값 : Long messageId
    warning : 이것은... 기존 멤버의 일반적인 chat 타입 메시지를 저장하는 함수이다.

     */

    public ChatMessageDto saveMessage(Long chatRoomId, ChatMessageCreateDto dto){
        System.out.println("come in\n");
        // 채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);
        System.out.println("get the chatroom obj\n");
        // 멤버 아이디 가져오기
        String chatName = dto.getWriterChatName();
        Long memberId = chatRoomMemberRepository.findByChatName(chatName).getMember().getId();
        System.out.println("get the member ID\n");

        // 메시지 객체 생성
        ChatMessage message = new ChatMessage(
                memberId,
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
    기능 : DB 에 저장된 메시지 리스트를 조회하여 반환한다.
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

    // DeleteMessage

    public void deleteMessage(Long messageId, Long memberId){
        // 원하는 메시지 불러오기

        // 메시지 삭제하기
    }









}
