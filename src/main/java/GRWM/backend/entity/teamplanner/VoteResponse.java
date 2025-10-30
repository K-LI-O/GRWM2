package GRWM.backend.entity.teamplanner;

import GRWM.backend.config.AvailableDateTimeConverter;
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
public class VoteResponse {

    /*
Long responseId,
TimeVote timeVote,
Member member,
List<AvailableDateTime> availableDateTimes

     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_response_id")
    @Setter(AccessLevel.NONE)
    private Long responseId;

    @ManyToOne(fetch = FetchType.LAZY)
    private TimeVote timeVote;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Convert(converter = AvailableDateTimeConverter.class)
    List<AvailableDateTime> availableDateTimes;

    public VoteResponse(TimeVote timeVote, Member member, List<AvailableDateTime> availableDateTimes){
        this.timeVote = timeVote;
        this.member = member;
        this.availableDateTimes = availableDateTimes;
    }

}
