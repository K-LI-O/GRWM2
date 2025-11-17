package GRWM.backend.entity.teamplanner;

import GRWM.backend.dto.teamPlanner.TeamCategoryDto;
import GRWM.backend.dto.teamPlanner.TeamMemberBriefDto;
import GRWM.backend.dto.teamPlanner.TeamScheduleBriefDto;
import GRWM.backend.dto.teamPlanner.TeamScheduleSearchByMemberDto;
import GRWM.backend.repository.teamplanner.TeamMemberRepository;
import GRWM.backend.repository.teamplanner.TeamPlannerRepository;
import GRWM.backend.repository.teamplanner.TeamScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamSearchService {
    private final TeamScheduleRepository scheduleRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamPlannerRepository plannerRepository;
    /*
      /*
    name : searchByKeyword
    function : 키워드로 일정 검색
    url : GET /api/team-planner/{plannerId}/search?keyword={keyword}
    Request: Long plannerId, String keyword
    Response: List<TeamScheduleBriefDto>
    */
    public List<TeamScheduleBriefDto> searchByKeyword(Long plannerId, String keyword){
        // 일정 목록 불러오기
        List<TeamSchedule> schedules = scheduleRepository.findByTeamPlanner_IdAndTitleContainingOrderByStartTimeDesc(plannerId, keyword);
        // dto 목록으로 변환
        return scheduleToDto(plannerId, schedules);

    }


    /*
    name : searchByMember
    function : 사용자별로 생성일정 및 참여일정 검색
    url : GET /api/team-planner/{plannerId}/search?userId = {userId}
    Request: Long plannerId, userId
    Response:
{
List<TeamScheduleBriefDto> createdSchedules, (사용자가 생성한 스케줄)
List<TeamScheduleBriefDto> joinedSchedules (사용자가 생성하진 않았지만 참여)
}
    */
    public TeamScheduleSearchByMemberDto searchByMember(Long plannerId, Long userId) {

        // 생성자로 일정 검색
        List<TeamSchedule> createdSchedules = scheduleRepository.findByTeamPlanner_IdAndCreator_IdOrderByStartTimeDesc(plannerId, userId);
        // 참여자 id로 일정 검색(생성자가 아니고)
        List<TeamSchedule> joinedSchedules = scheduleRepository.findByNotCreatorAndMemberIncluded(plannerId, userId, userId);


        return TeamScheduleSearchByMemberDto.builder()
                .createdSchedules(scheduleToDto(plannerId, createdSchedules))
                .joinedSchedules(scheduleToDto(plannerId, joinedSchedules))
                .build();
    }

    private List<TeamScheduleBriefDto> scheduleToDto(Long plannerId, List<TeamSchedule> schedules) {
        List<TeamScheduleBriefDto> result = new ArrayList<>();
        for(TeamSchedule t : schedules) {

            // 카테고리와 생성자 dto 만들기
            TeamCategoryDto categoryDto = TeamCategoryDto.builder().build();
            if (t.getCategory() != null) {
                TeamCategory cat = t.getCategory();
                categoryDto = TeamCategoryDto.builder()
                        .categoryId(cat.getId())
                        .categoryName(cat.getName())
                        .color(cat.getColor())
                        .build();
            }

            TeamMemberBriefDto memberBriefDto = TeamMemberBriefDto.builder().build();
            if (t.getCreator() != null) {
                TeamMember member = teamMemberRepository.findByTeamPlannerAndMember(plannerRepository.getReferenceById(plannerId), t.getCreator());
                memberBriefDto = TeamMemberBriefDto.builder()
                        .userId(member.getMember().getId())
                        .username(member.getMember().getUsername())
                        .profileImage(member.getMember().getProfileImageLink())
                        .status(member.getStatus())
                        .build();
            }

            TeamScheduleBriefDto dto = TeamScheduleBriefDto.builder()
                    .scheduleId(t.getId())
                    .category(categoryDto)// 널일 때 처리
                    .title(t.getTitle())
                    .creator(memberBriefDto)
                    .startDateTime(t.getStartTime())
                    .finishDateTime(t.getFinishTime())
                    .location(t.getLocation())
                    .build();
            result.add(dto);
        }
        return result;
    }
}

