package GRWM.backend.controller.teamplanner;

import GRWM.backend.dto.teamPlanner.AvailableDateTimeDto;
import GRWM.backend.dto.teamPlanner.TimeVoteBriefDto;
import GRWM.backend.dto.teamPlanner.TimeVoteCreateDto;
import GRWM.backend.dto.teamPlanner.TimeVoteDetailDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.teamplanner.TimeVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimeVoteController {

    private final TimeVoteService timeVoteService;

    /*
    name : createTimeVote
    POST/api/team-planner/{plannerId}/time-vote
    param : Long plannerId
    TimeVoteCreateDto
    return value : Long voteId
     */
    @PostMapping("/api/team-planner/{plannerId}/time-vote")
    public Long createTimeVote(@PathVariable Long plannerId, @RequestBody TimeVoteCreateDto dto) throws Exception {
        return timeVoteService.createTimeVote(plannerId, dto);
    }

    /*
    name : vote
    POST /api/team-planner/{plannerId}/time-vote/{voteId}
    param : Long plannerId, Long voteId
    List<AvailableDateTimeDto>
    return value : TimeVoteDetailDto
    * 마감 기한 이후에는 투표 불가
    */
    @PostMapping("")
    public TimeVoteDetailDto vote(@PathVariable Long plannerId, @PathVariable Long voteId,
                                  @RequestBody List<AvailableDateTimeDto> dtoList,
                                  @AuthenticationPrincipal CustomUserDetails userDetails){
        return timeVoteService.vote(plannerId, voteId, dtoList, userDetails.getUserId());
    }



    /*
    name : updateTimeVote
    PUT /api/team-planner/{plannerId}/time-vote/{voteId}
    param : Long plannerId, Long voteId
    List<AvailableDateTimeDto>
    return value : TimeVoteDetailDto
    */
    @PutMapping("/api/team-planner/{plannerId}/time-vote/{voteId}")
    public TimeVoteDetailDto updateTimeVote(@PathVariable Long plannerId, @PathVariable Long voteId,
                                            @RequestBody List<AvailableDateTimeDto> dtoList,
                                            @AuthenticationPrincipal CustomUserDetails userDetails){
        return timeVoteService.updateTimeVote(plannerId, voteId, dtoList, userDetails.getUserId());
    }

    /*
    name : showTimeVoteList
    GET /api/team-planner/{plannerId}/time-vote
    param : Long plannerId
    return value : List<TimeVoteBriefDto>
    */
    @GetMapping("/api/team-planner/{plannerId}/time-vote")
    public List<TimeVoteBriefDto> showTimeVoteList(@PathVariable Long plannerId){
        return timeVoteService.getTimeVoteList(plannerId);
    }


    /*
    name : showTimeVoteDetail
    GET /api/team-planner/{plannerId}/time-vote/{voteId}
    param : Long plannerId, Long voteId
    return value : TimeVoteDetailDto
     */
    @GetMapping("/api/team-planner/{plannerId}/time-vote/{voteId}")
    public TimeVoteDetailDto showTimeVoteDetail(@PathVariable Long plannerId, @PathVariable Long voteId){
        return timeVoteService.showTimeVoteDetail(plannerId, voteId);
    }

}

