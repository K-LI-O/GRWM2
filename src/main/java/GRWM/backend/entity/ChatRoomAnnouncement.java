package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
public class ChatRoomAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_announcement_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String content;


    // 공지 생성일
    @CreatedDate
    private LocalDateTime createdAt;

    // 채팅방과 공지는 일대다 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    private String writerChatName;

    // 작성자와 공지는 일대다 관계


    public ChatRoomAnnouncement(String content, ChatRoom chatroom, String writerChatName){
        this.content = content;
        this.chatRoom = chatroom;
        this.writerChatName = writerChatName;
    }
}
