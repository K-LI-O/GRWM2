package GRWM.backend.entity.chatroom;

import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomCommunity {

    @Id // 단일 인공 키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private CommunityUser communityUser;

    @CreatedDate
    private LocalDateTime createdAt;

    private boolean isManager;

    public ChatRoomCommunity(ChatRoom chatRoom, CommunityUser communityUser, boolean isManager){
        this.chatRoom = chatRoom;
        this.communityUser = communityUser;
        this.isManager = isManager;

    }


}
