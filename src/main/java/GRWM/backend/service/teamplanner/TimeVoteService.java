package GRWM.backend.service.teamplanner;


import GRWM.backend.dto.teamPlanner.*;
import GRWM.backend.entity.teamplanner.*;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.teamplanner.TeamMemberRepository;
import GRWM.backend.repository.teamplanner.TeamPlannerRepository;
import GRWM.backend.repository.teamplanner.TimeVoteRepository;
import GRWM.backend.repository.teamplanner.VoteResponseRepository;
import GRWM.backend.repository.user.MemberRepository;
import GRWM.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeVoteService {

    private final TimeVoteRepository timeVoteRepository;
    private final VoteResponseRepository voteResponseRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamPlannerRepository teamPlannerRepository;



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
    public Long createTimeVote(Long plannerId, TimeVoteCreateDto dto) throws Exception {
        TimeVote timeVote = TimeVote.builder()
                .title(dto.getTitle())
                .voteRange(dto.getVoteRange())
                .finishTime(dto.getFinishTime())
                .memberIds(dto.getMemberIds())
                .build();

        List<Member> members = new ArrayList<>();


        TimeVote savedVote = timeVoteRepository.save(timeVote);
        for(Long id : dto.getMemberIds()){
            members.add(memberRepository.findById(id).orElseThrow());
        }
//        notificationService.createTimeVoteNotification(
//                members,
//                teamPlannerRepository.findById(plannerId).orElseThrow(),
//                savedVote);
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
    public TimeVoteDetailDto vote(Long plannerId, Long voteId, List<AvailableDateTimeDto> dtoList, Long userId){
        // 시간 투표 찾기
        TimeVote timeVote = extractOptionalVote(voteId);

        // 멤버가 투표자에 포함되는지 확인
        List<Long> memberIds = timeVote.getMemberIds();
        if(!memberIds.contains(userId)){
            throw new RuntimeException("투표 불가능한 멤버입니다.");
        }
        // 투표자라면 voteResponse 객체 생성하여 저장.
        List<AvailableDateTime> dateTimes = new ArrayList<>();
        for(AvailableDateTimeDto t : dtoList) {
            dateTimes.add(getAvailableDateTime(t));
        }
        VoteResponse response = VoteResponse.builder()
                .timeVote(timeVote)
                .member(extractOptionalMember(userId))
                .availableDateTimes(dateTimes)
                .build();
        VoteResponse savedResponse = voteResponseRepository.save(response);

        // vote response 를 반영하여 시간 투표 수정
        List<VoteResponse> voteResponses = timeVote.getVoteResponses();
        voteResponses.add(savedResponse);
        timeVote.setVoteResponses(voteResponses);
        TimeVote savedVote = timeVoteRepository.save(timeVote);

        // 투표 결과 계산해서(voteResponseDto 계산 함수 private 함수로 따로 뺄 것)
        // 멤버 반환하기
        VoteResponseDto result = VoteResponseDto.builder()
                .responseId(savedResponse.getResponseId())
                .voteId(savedVote.getId())
                .member(getMemberDto(teamPlannerRepository.getReferenceById(plannerId), savedResponse.getMember()))
                .availableDateTime(dtoList)
                .build();

        // 결과 반환
        return getTimeVoteDetailDto(plannerId, savedVote);
    }

    /*
    name : updateTimeVote
    PUT /api/team-planner/{plannerId}/time-vote/{voteId}
    param : Long plannerId, Long voteId
    List<AvailableDateTimeDto>
    return value : VoteResponseDto
    */
    public TimeVoteDetailDto updateTimeVote(Long plannerId, Long voteId, List<AvailableDateTimeDto> dtoList, Long userId){
        // vote 찾아오기
        TimeVote timeVote = extractOptionalVote(voteId);

        List<Long> memberIds = timeVote.getMemberIds();
        if(!memberIds.contains(userId)){
            throw new RuntimeException("투표 불가능한 멤버입니다.");
        }
        // 사용자의 투표 찾기
        List<AvailableDateTime> dateTimes = new ArrayList<>();
        for(AvailableDateTimeDto t: dtoList){
            dateTimes.add(getAvailableDateTime(t));

        }

        VoteResponse response = null;
        List<VoteResponse> responses = timeVote.getVoteResponses();
        for(VoteResponse t: responses){
            if(t.getMember().getId().equals(userId)){
                t.setAvailableDateTimes(dateTimes); // 객체 업데이트
                response = voteResponseRepository.save(t);

            }
        }
        TimeVote savedVote = timeVoteRepository.save(timeVote);
        // 결과 반환
        return getTimeVoteDetailDto(plannerId, savedVote);
    }

    // 최신 시간 투표 목록 보기(5개)
    /*
    name : showTimeVoteList
    GET /api/team-planner/{plannerId}/time-vote
    param : Long plannerId
    return value : List<TimeVoteBriefDto>
    */
    public List<TimeVoteBriefDto> getTimeVoteList(Long plannerId){
        Pageable pageable = PageRequest.of(
                0, // 페이지 번호 (첫 번째 페이지는 0)
                5, // 페이지 크기 (가져올 항목 수: 5개)
                Sort.by(Sort.Direction.DESC, "createdAt") // 정렬 기준 (createdAt 필드를 내림차순(DESC, 최신 순)으로)
        );
        List<TimeVote> votes = timeVoteRepository.findByTeamPlanner(teamPlannerRepository.getReferenceById(plannerId), pageable);
        List<TimeVoteBriefDto> result = new ArrayList<>();
        for(TimeVote tv : votes){
            result.add(getTimeVoteBriefDto(tv));
        }
        return result;
    }

    // 시간 투표 디테일 보기
    /*
    name : showTimeVoteDetail
    GET /api/team-planner/{plannerId}/time-vote/{voteId}
    param : Long plannerId, Long voteId
    return value : TimeVoteDetailDto
     */
    public TimeVoteDetailDto showTimeVoteDetail(Long plannerId, Long voteId){
        TimeVote vote = extractOptionalVote(voteId);
        return getTimeVoteDetailDto(plannerId, vote);
    }


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
    private List<TimeVoteShowDto> colorTimeTable(TimeVote vote){

        // 투표 response 불러오기
        List<VoteResponse> responses = vote.getVoteResponses();

        // max 5days, 48 segments per day, vote에 기록된 일별로 48 segments씩 세팅
        // segments 0으로 초기화
        List<TimeVoteShowDto> result = new ArrayList<>();
        for(LocalDate d : vote.getVoteRange()){
            for(int i = 0; i < 48; i++){
                TimeVoteShowDto dto = TimeVoteShowDto.builder()
                        .date(d)
                        .slotStart(LocalTime.MIN.plusMinutes(i * 30)) // LocalTime
                        .slotEnd(LocalTime.MIN.plusMinutes(i * 30 + 30))
                        .overlapCount(0)
                        .overlapPercentage(0.0)
                        .build();
                result.add(dto);
            }
        }
        // for 문으로 반복하여 가공하기 (private 함수화)
        for(VoteResponse t : responses){
            // each response 를 30분짜리 segments 로 분리
            // 해당하는 segment 카운트 추가하기
            for(AvailableDateTime a : t.getAvailableDateTimes()){
                a.getDate();
            }
            /*
LocalDate date, // 날짜
LocalTime slotStart, // 시간대 시작
LocalTime slotEnd, // 시간대 끝 (30분 간격)
int overlapCount, // 해당 시간대에 투표한 사람
double overlapPercentage, // (해당 시간대에 투표한 사람) / (전체 투표자)
             */

        }
        // 반환
        return result;
    }


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


    // ======= private logics ======= //

    private TimeVote extractOptionalVote(Long voteId){
        // 시간 투표 찾기
        Optional<TimeVote> optionalVote = timeVoteRepository.findById(voteId);
        if(optionalVote.isEmpty()){
            throw new RuntimeException("존재하지 않는 시간 투표입니다.");
        }
        return optionalVote.get();

    }

    private AvailableDateTime getAvailableDateTime(AvailableDateTimeDto t) {
        List<Interval> intervals = new ArrayList<>();
        for (IntervalDto i : t.getIntervals()) {
            Interval interval = Interval.builder()
                    .startTime(i.getStartTime())
                    .endTime(i.getEndTime())
                    .build();
            intervals.add(interval);
        }
        AvailableDateTime availableDateTime = AvailableDateTime.builder()
                .date(t.getDate())
                .intervals(intervals)
                .build();
        return availableDateTime;
    }

    private TeamMemberBriefDto getMemberDto(TeamPlanner planner, Member t){

        TeamMemberBriefDto dto = TeamMemberBriefDto.builder()
                .userId(t.getId())
                .username(t.getUsername())
                .profileImage(t.getProfileImageLink())
                .status(getStatus(planner, t))
                .build();

        return dto;
    }

    private String getStatus(TeamPlanner planner, Member member){
        return teamMemberRepository.findByTeamPlannerAndMember(planner, member).getStatus();
    }

    private TimeVoteDetailDto getTimeVoteDetailDto(Long plannerId, TimeVote vote){
        List<TeamMemberBriefDto> members = new ArrayList<>();
        for(Long id : vote.getMemberIds()){
            members.add(getMemberDto(
                    teamPlannerRepository.getReferenceById(plannerId),
                    extractOptionalMember(id))
            );
        }

        TimeVoteDetailDto dto = TimeVoteDetailDto.builder()
                .title(vote.getTitle())
                .voteRange(vote.getVoteRange())
                .members(members)
                .matrix(colorTimeTable(vote))
                .build();

        return dto;
    }

    private TimeVoteBriefDto getTimeVoteBriefDto(TimeVote vote){
        TimeVoteBriefDto dto = TimeVoteBriefDto.builder()
                .id(vote.getId())
                .title(vote.getTitle())
                .voteRange(vote.getVoteRange())
                .finishTime(vote.getFinishTime())
                .build();

        return dto;
    }


}
