package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeVoteDto {

    /*
{
String title,
List<LocalDate> voteRange, 투표 범위 5일(떨어진 날짜 가능)
LocalDateTime finishTime, 마감 기한
List<Long> memberIds (투표에 참여하는 사람들의 id 목록)
List<VoteResponse> voteResponses (형태는 위에 예시)
}
     */

    private String title;
    private List<LocalDate> voteRange;
    private List<Long> memberIds;
    private List<VoteResponseDto> voteResponses;
}
