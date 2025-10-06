package GRWM.backend.dto.teamPlanner;

import GRWM.backend.entity.teamplanner.AvailableDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponseDto {
    /*
Long responseId,
Long voteId,
MemberDto member,
List<AvailableDateTime> availableDateTime, (사용자가 투표한 시간)
List<AvailableDateTime> bestDateTime, 득표율이 가장 높은 시간 or 모든 멤버가 투표한 시간
}
     */

    private Long responseId;
    private Long voteId;
    private TeamMemberBriefDto member;
    private List<AvailableDateTimeDto> availableDateTime;
    private List<AvailableDateTimeDto> bestDateTime;
}
