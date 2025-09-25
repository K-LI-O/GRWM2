package GRWM.backend.repository;

import GRWM.backend.entity.chatroom.ChatRoomTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomTagRepository extends JpaRepository<ChatRoomTag, Long> {


    boolean existsByContent(String content);

    ChatRoomTag findByContent(String content);



}
