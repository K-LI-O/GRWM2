package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_announcement_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String content;

    private boolean isMain;


    public ChatRoomAnnouncement(String content, boolean isMain){
        this.content = content;
        this.isMain = isMain;
    }
}
