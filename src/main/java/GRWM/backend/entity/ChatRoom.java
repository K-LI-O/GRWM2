package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private boolean isPrivate; // 공개(false) / 비공개방(true) = 비밀번호 유무와 동일
    @Column
    private String password;  // 비공개방인 경우 입장 비밀번호 (서버에서만 사용)
    @Column
    private int maxMembers;    // 최대 참여 가능 인원 (0 = 무제한)
    @Column
    private int currentMembers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_member_id")
    private Member owner;


    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

//    @ManyToMany
//    private List<ChatRoomTag> chatRoomTags = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomAnnouncement> announcements = new ArrayList<>();

    // 생성자

    public ChatRoom(String name, String description, boolean isPrivate, String password, int maxMembers, Member owner){
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
        this.password = password;
        this.maxMembers = maxMembers;
        this.owner = owner;
    }

}
