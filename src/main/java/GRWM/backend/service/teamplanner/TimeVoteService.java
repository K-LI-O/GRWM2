package GRWM.backend.service.teamplanner;


import GRWM.backend.dto.teamPlanner.TimeVoteCreateDto;
import GRWM.backend.entity.teamplanner.TimeVote;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.teamplanner.TimeVoteRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeVoteService {

    private final TimeVoteRepository timeVoteRepository;
    private final MemberRepository memberRepository;


    /*
    name : createTimeVote
    POST/api/team-planner/{plannerId}/time-vote
    param : Long plannerId
{
String title,
List<LocalDate> voteRange, (투표 범위 5일(떨어진 날짜 가능))
LocalDateTime finishTime, (마감 기한}
List<Long> memberIds (투표에 참여하는 사람들의 id 목록)
}
    return value : Long voteId
    */
    public Long createTimeVote(Long plannerId, TimeVoteCreateDto dto){
        TimeVote timeVote = TimeVote.builder()
                .title(dto.getTitle())
                .voteRange(dto.getVoteRange())
                .finishTime(dto.getFinishTime())
                .memberIds(dto.getMemberIds())
                .build();

        TimeVote savedVote = timeVoteRepository.save(timeVote);
        return savedVote.getId();
    }

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


    // ======== private logics ======= //

    private Member extractOptionalMember(Long userId) throws RuntimeException{
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if(optionalMember.isPresent()){
            return optionalMember.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }
}
