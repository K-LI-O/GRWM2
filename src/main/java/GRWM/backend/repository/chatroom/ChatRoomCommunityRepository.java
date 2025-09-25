package GRWM.backend.repository.chatroom;

import GRWM.backend.entity.chatroom.ChatRoomCommunity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomCommunityRepository extends JpaRepository<ChatRoomCommunity, Long> {
}
