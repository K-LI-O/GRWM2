package GRWM.backend.repository;

import GRWM.backend.entity.ChatRoomTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomTagRepository extends JpaRepository<ChatRoomTag, Long> {


    boolean existsByContent(String content);

    ChatRoomTag findByContent(String content);



}
