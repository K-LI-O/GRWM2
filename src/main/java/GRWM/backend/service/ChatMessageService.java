package GRWM.backend.service;

import GRWM.backend.dto.ChatMessageCreateDto;
import GRWM.backend.dto.ChatMessageDto;
import GRWM.backend.entity.ChatMessage;
import GRWM.backend.entity.ChatRoom;
import GRWM.backend.repository.ChatMessageRepository;
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

    /*
    함수명 : saveMessage
    기능 : 메시지를 DB 에 저장하고 리턴한다.
    매개변수 : Long chatRoomId, chatMessageCreateDTO
    반환값 : Long messageId

     */

    public Long saveMessage(Long chatRoomId, ChatMessageCreateDto dto){

        // 채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);

        // 메시지 객체 생성
        ChatMessage message = new ChatMessage(dto.getContent(), chatRoom, dto.getWriterChatName());
        ChatMessage savedMessage = messageRepository.save(message);

        return savedMessage.getId();
    }


    /*
    함수명 : showMessage
    기능 : 메시지를 DB 에 저장하고 리턴한다.
    매개변수 : Long chatRoomId,
    반환값 : chatMessageDTO


     */

    public List<ChatMessageDto> showMessageList(Long chatRoomId){

        List<ChatMessage> messageList = messageRepository.findByChatRoom_IdOrderByCreatedAtDesc(chatRoomId);
        List<ChatMessageDto> dtoList = new ArrayList<>();

        for(ChatMessage chatMessage : messageList){
            ChatMessageDto dto = new ChatMessageDto(chatMessage.getId(),
                    chatMessage.getContent(),
                    chatMessage.getCreatedAt(),
                    chatMessage.getWriterChatName());

            dtoList.add(dto);

        }

        return dtoList;
    }











}
