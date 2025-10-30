package GRWM.backend.entity.studyroom;

import GRWM.backend.entity.user.CommunityUser;
import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRoomMember> members = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRoomTodo> todoList = new ArrayList<>(); //스터디룸에서 작성하는 투두리스트.

    private int duration;// 지속 시간(분 단위)
    private int extensionTime; //연장 시 몇 분 연장되는지
    private int extensionCount; // 최대 n회 연장 가능하며, 해당 방이 몇 회나 연장했는지의 count;
    private boolean isAvailable; // 조회할 때 유효한 걸 가져와야 하니까...
}
