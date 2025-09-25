package GRWM.backend.repository;

import GRWM.backend.entity.chatroom.ChatRoomAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomAnnouncementRepository extends JpaRepository<ChatRoomAnnouncement, Long> {

    ChatRoomAnnouncement findFirstByChatRoom_IdOrderByCreatedAtDesc(Long chatRoomId);

}
