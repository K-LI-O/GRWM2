package GRWM.backend.dto.teamPlanner;

import GRWM.backend.entity.teamplanner.AvailableDateTime;
import lombok.*;
import org.checkerframework.common.value.qual.BottomVal;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteResponseDto {
    /*
Long responseId,
Long voteId,
MemberDto member,
List<AvailableDateTime> availableDateTime, (사용자가 투표한 시간)
     */

    private Long responseId;
    private Long voteId;
    private TeamMemberBriefDto member;
    private List<AvailableDateTimeDto> availableDateTime;
    //private List<AvailableDateTimeDto> bestDateTime;
}
