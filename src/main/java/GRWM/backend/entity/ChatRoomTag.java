package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_tag_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToMany(mappedBy = "chatRoomTags") // ChatRoom 엔티티의 "chatRoomTags" 필드에 의해 매핑됨
    private Set<ChatRoom> chatRooms = new HashSet<>();

}
