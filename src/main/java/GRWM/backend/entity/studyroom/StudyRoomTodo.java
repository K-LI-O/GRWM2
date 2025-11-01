package GRWM.backend.entity.studyroom;


import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StudyRoomTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_room_todo_id")
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private StudyRoom studyRoom;

    @OneToOne
    private CommunityUser creator;

    private String title;
    private String description;
    private boolean isCompleted;
    @OneToMany(mappedBy = "todo")
    private List<Reaction> reactions = new ArrayList<>(); //스터디룸에서 작성하는 투두리스트.
 // 각 투두 항목에 붙는 리액션 이모지 목록

}
