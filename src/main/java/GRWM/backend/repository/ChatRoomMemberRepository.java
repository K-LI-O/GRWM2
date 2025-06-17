package GRWM.backend.repository;

import GRWM.backend.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    List<ChatRoomMember> findByMember_Id(Long memberId);


    ChatRoomMember findByMember_IdAndChatRoom_Id (Long memberId, Long chatRoomId);

}
