package GRWM.backend.service.chatroom;


import GRWM.backend.dto.ChatRoomTagCreateDto;
import GRWM.backend.entity.chatroom.ChatRoomTag;
import GRWM.backend.repository.chatroom.ChatRoomTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomTagService {


    private final ChatRoomTagRepository chatRoomTagRepository;

    /*
    함수명 : createChatRoomTags
    기능 : 카테고리 생성; 아마 맨 처음에
    param : dto -
        String categoryName
        String description
    return value : Long tagId
     */

    public Long createChatRoomTags(String categoryName) throws Exception {

        ChatRoomTag tag = new ChatRoomTag(categoryName);

        // 이미 존재하는 동명이객체가 있는지 확인
        boolean tagExists =  chatRoomTagRepository.existsByContent(categoryName);

        if(tagExists){
            throw new Exception("이미 존재하는 채팅방 카테고리입니다.");
        }

        ChatRoomTag newTag = chatRoomTagRepository.save(tag);
        return newTag.getId();
    }


    /*
    함수명 : deleteChatRoomTags
    기능 : 카테고리 삭제
    param :
        Long tagId
    return value : status 403

     */

    public void deleteChatRoomTags(Long tagId) throws Exception {
        Optional<ChatRoomTag> tag = chatRoomTagRepository.findById(tagId);
        // optional 존재 여부 확인
        if (tag.isPresent()) {
            chatRoomTagRepository.deleteById(tagId);
        } else {
            throw new Exception("존재하지 않는 채팅방 카테고리입니다.");
        }
    }


    /* get
    함수명 : findChatRoomTags
    기능 : 이미 존재하는 카테고리 목록 조회
    param : Dto
        Long tagId
        String categoryName
    return value : List<dto>

     */

    public List<ChatRoomTagCreateDto> findAllChatRoomTags(){
        List<ChatRoomTag> tagList = chatRoomTagRepository.findAll();


        // dtoList 생성하기
        List<ChatRoomTagCreateDto> dtoList = new ArrayList<>();
        for (ChatRoomTag tag : tagList) { // List to Dto

            // 새로운 Dto 생성하여 tag의 값 저장하기;
            ChatRoomTagCreateDto dto = new ChatRoomTagCreateDto(tag.getContent());

            // dtoList에 dto 저장
            dtoList.add(dto);

        }

        return dtoList;
    }


    /* GET, PATCH
    함수명 : findOneTag
    기능 : 카테고리 수정
    param : Dto
        String categoryName
    return value : status

     */

    // find individnal tag
    public ChatRoomTagCreateDto findOneTag(Long tagId) throws Exception {
        // optional로 객체 꺼내기
        Optional<ChatRoomTag> optionalTag =  chatRoomTagRepository.findById(tagId);

        // 카테고리를 객체화; 없다면 예외 반환;
        ChatRoomTag tag = new ChatRoomTag();
        if(optionalTag.isPresent()){
            tag = optionalTag.get();
        } else{
            throw new Exception("존재하지 않는 채팅방 카테고리입니다.");
        }

        // dto에 담아 반환;
        ChatRoomTagCreateDto dto = new ChatRoomTagCreateDto(tag.getContent());
        return dto;
    }


    /* PATCH
    함수명 : updateChatRoomTags
    기능 : 카테고리 수정
    param : Dto
        String categoryName
    return value : status

     */

    public void updateChatRoomTag(Long tagId, ChatRoomTagCreateDto dto) throws Exception{

        // 기존 객체 불러오기
        Optional<ChatRoomTag> optionalTag = chatRoomTagRepository.findById(tagId);

        // 카테고리를 객체화; 없다면 예외 반환;
        ChatRoomTag tag = new ChatRoomTag();
        if(optionalTag.isPresent()){
            tag = optionalTag.get();
        } else{
            throw new Exception("존재하지 않는 채팅방 카테고리입니다.");
        }

        tag.setContent(dto.getTagName());
        chatRoomTagRepository.save(tag);
    }


}
