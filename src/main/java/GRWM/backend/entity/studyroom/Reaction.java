package GRWM.backend.entity.studyroom;

import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_reaction_id")
    @Setter(AccessLevel.NONE)
    private Long id;
    @OneToOne
    private CommunityUser reactor;
    private String reaction;

    @ManyToOne(cascade = CascadeType.ALL)
    private StudyRoomTodo todo;


}
