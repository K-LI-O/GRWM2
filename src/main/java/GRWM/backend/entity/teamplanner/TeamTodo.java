package GRWM.backend.entity.teamplanner;

import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamTodo {
//    /*
//    Long Id
//Schedule schedule
//member member
//String content,
//boolean isCompleted
//boolean isPrivate
//
//     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_todo_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamSchedule teamSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creator;

    private String content;

    private boolean isCompleted;

    private boolean isPrivate;

    public TeamTodo(TeamSchedule teamSchedule, Member creator, String content, boolean isPrivate){
        this.teamSchedule = teamSchedule;
        this.creator = creator;
        this.content = content;
        this.isCompleted = false;
        this.isPrivate = isPrivate;
    }

}
