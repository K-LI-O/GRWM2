package GRWM.backend.entity.teamplanner;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeVote {

    /*
    Long voteId,
    TeamPlanner teamPlanner,
    String title,
    List<LocalDate> voteRange, 투표 범위인 5일 선택(떨어진 날짜 가능)
    LocalDateTime finishTime, 마감 기한
    List<VoteResponse> voteResponses

     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_vote_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamPlanner teamPlanner;

    private String title;

    private List<LocalDate> voteRange;

    private LocalDateTime finishTime;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VoteResponse> voteResponses;

    private List<Long> memberIds;

    public TimeVote(TeamPlanner teamPlanner, String title, List<LocalDate> voteRange,
                    LocalDateTime finishTime, List<VoteResponse> voteResponses){
        this.teamPlanner = teamPlanner;
        this.title = title;
        this.voteRange = voteRange;
        this.finishTime = finishTime;
        this.voteResponses = voteResponses;
    }
}

