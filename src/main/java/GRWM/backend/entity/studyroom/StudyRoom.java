package GRWM.backend.entity.studyroom;

import GRWM.backend.entity.user.CommunityUser;
import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class StudyRoom {
    /*
    Studyroom
{
Long id;
String name;
CommunityUser creator;
String category;
String description;
List<CommunityUser> users;
int duration; 지속 시간(분 단위)
int extensionTime; 연장 시 몇 분 연장되는지
List<StudyRoomTodo> todoList; 스터디룸에서 작성하는 투두리스트.
int extensionCount; // 최대 n회 연장 가능하며, 해당 방이 몇 회나 연장했는지의 count;
}

     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_room_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    @ManyToOne
    private CommunityUser creator;
    private String category;
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.ALL)
    private List<StudyRoomMember> members = new ArrayList<>();
    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StudyRoomTodo> todoList = new ArrayList<>(); //스터디룸에서 작성하는 투두리스트.

    @CreatedDate
    private LocalDateTime createdAt;

    private int duration;// 지속 시간(분 단위)
    @Builder.Default
    private int extensionTime = 0; //연장 시 몇 분 연장되는지
    private int extensionCount; // 최대 n회 연장 가능하며, 해당 방이 몇 회나 연장했는지의 count;
    private boolean isActive; // 조회할 때 유효한 걸 가져와야 하니까...

    // =========== //

    @Builder.Default
    int memberCount = 0; //(전체 멤버 수)
    @Builder.Default
    int voteCount = 0; // 투표한 (현재 멤버 수)
    @Builder.Default
    int agreedCount = 0;
}
