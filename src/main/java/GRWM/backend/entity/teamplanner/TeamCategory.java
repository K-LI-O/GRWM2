package GRWM.backend.entity.teamplanner;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class TeamCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "team_category_id")
    private Long Id;

    private String name;
    private String color;

    @OneToMany
    List<TeamSchedule> schedules;

    @ManyToOne
    @JoinColumn(name = "team_planner_id")
    TeamPlanner teamPlanner;

    public TeamCategory(String name, String color){
        this.name = name;
        this.color = color;
    }
}
