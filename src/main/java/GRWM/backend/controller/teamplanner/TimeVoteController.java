package GRWM.backend.controller.teamplanner;

import GRWM.backend.service.teamplanner.TimeVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TimeVoteController {

    private final TimeVoteService timeVoteService;

    /*
    name : createTimeVote
    POST/api/team-planner/{plannerId}/time-vote
    param : Long plannerId
{ TimeVoteCreateDto
String title,
List<LocalDate> voteRange, (투표 범위 5일(떨어진 날짜 가능))
LocalDateTime finishTime, (마감 기한}
List<Long> memberIds (투표에 참여하는 사람들의 id 목록)
}
    return value : Long voteId
    */

    /*
    name : vote
    POST /api/team-planner/{plannerId}/time-vote/{voteId}
    param : Long plannerId, Long voteId
    List<AvailableDateTimeDto>
    return value : VoteResponseDto
    * 마감 기한 이후에는 투표 불가
    */

    /*
    name : 시간 재투표(업데이트)
    PUT /api/team-planner/{plannerId}/time-vote/{voteId}
    param : Long plannerId, Long voteId
    List<AvailableDateTimeDto>
    return value : VoteResponseDto
    */

    /*
    name : colorTimeTable
    GET /api/team-planner/{plannerId}/time-vote/{voteId}
    param : Long plannerId, Long voteId
    return value : TimeVoteDto
List<
{
LocalDate date,
LocalTime slotStart,
LocalTime slotEnd,
int overlapCount,
double overlapPercentage,
}
>
     */
}
