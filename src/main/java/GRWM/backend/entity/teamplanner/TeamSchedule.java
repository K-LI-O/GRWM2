package GRWM.backend.entity.teamplanner;

import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeamSchedule {

    /*
    {
String location,
String memo,
String editorRange (수정자 범위; creator, creatorAndManager, everyone)
List<Member> members,
List<TodoDto> todoList,
}
     */
    @Id
    @GeneratedValue
    @Column(name = "team_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private TeamPlanner teamPlanner;

    @ManyToOne
    @JoinColumn
    private TeamCategory category;

    private String title;
    private String location;
    private String memo;
    private String editorRange;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    List<Long> memberIds;

    @OneToMany(mappedBy = "teamSchedule")
    List<TeamTodo> todos;

}
