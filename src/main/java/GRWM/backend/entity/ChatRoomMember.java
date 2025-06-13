package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomMember {

    @Id // 단일 인공 키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    private LocalDateTime joinedAt;

    private String chatName;

    private String profileImageLink;

    public ChatRoomMember(ChatRoom chatRoom, Member member, LocalDateTime joinedAt, String chatName){
        this.chatRoom = chatRoom;
        this.member = member;
        this.joinedAt = joinedAt;
        this.chatName = chatName;
    }

}
