package GRWM.backend.repository.chatroom;

import GRWM.backend.entity.chatroom.ChatRoomCommunity;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomCommunityRepository extends JpaRepository<ChatRoomCommunity, Long> {
    List<ChatRoomCommunity> findByCommunityUser(CommunityUser user);
}
