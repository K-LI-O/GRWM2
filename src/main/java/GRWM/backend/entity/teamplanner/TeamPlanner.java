package GRWM.backend.entity.teamplanner;

import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPlanner {

    /*
Long plannerId,
Member creator,
List<Member> members,
String title,
String description,
String profileImageLink,
List<Schedules> schedules;
List<Category> categories;
List<TimeVote> timeVotes;

     */



    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "team_planner_id")
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creator;

    @OneToMany(mappedBy = "teamPlanner", orphanRemoval=true, cascade = CascadeType.REMOVE)
    private List<TeamMember> teamMembers;

    private String title;
    private String description;
    private String profileImageLink;

    @OneToMany(mappedBy = "teamPlanner", orphanRemoval=true)
    List<TeamSchedule> schedules;

    @OneToMany(mappedBy = "teamPlanner", orphanRemoval=true)
    List<TeamCategory> categories;

    @OneToMany(mappedBy = "teamPlanner", orphanRemoval=true)
    List<TimeVote> timeVotes;


}
