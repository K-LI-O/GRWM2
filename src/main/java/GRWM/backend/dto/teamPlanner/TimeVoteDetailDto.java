package GRWM.backend.dto.teamPlanner;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeVoteDetailDto {

    /*
{
String title,
List<LocalDate> voteRange, 투표 범위 5일(떨어진 날짜 가능)
LocalDateTime finishTime, 마감 기한
List<MemberBriefDto> members;
List<TimeVoteShowDto> matrix;
}
     */

    private String title;
    private List<LocalDate> voteRange;
    private LocalDateTime finishTime;
    private List<TeamMemberBriefDto> members;
    private List<TimeVoteShowDto> matrix;
}
