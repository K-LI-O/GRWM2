package GRWM.backend.service.teamplanner;



import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.dto.teamPlanner.TeamCategoryCreateDto;
import GRWM.backend.dto.teamPlanner.TeamCategoryDto;
import GRWM.backend.dto.teamPlanner.TeamMemberBriefDto;
import GRWM.backend.dto.teamPlanner.TeamScheduleBriefDto;
import GRWM.backend.entity.teamplanner.TeamCategory;
import GRWM.backend.entity.teamplanner.TeamPlanner;
import GRWM.backend.entity.teamplanner.TeamSchedule;
import GRWM.backend.repository.teamplanner.TeamCategoryRepository;
import GRWM.backend.repository.teamplanner.TeamMemberRepository;
import GRWM.backend.repository.teamplanner.TeamPlannerRepository;
import GRWM.backend.repository.teamplanner.TeamScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamCategoryService {

    private final TeamCategoryRepository categoryRepository;
    private final TeamPlannerRepository teamPlannerRepository;
    private final TeamScheduleRepository scheduleRepository;
    private final TeamMemberRepository teamMemberRepository;

        /*
    name : createCategory
    function : 카테고리 생성하기
    POST /api/team-planner/{plannerId}/category
    param : Long plannerId, TeamCategoryCreateDto
    return value : Long categoryId
    */

    public Long createCategory(Long plannerId, TeamCategoryCreateDto dto){
        // 이미 같은 이름의 카테고리가 존재하는지 확인
        if(categoryRepository.existsByName(dto.getName())){
            throw new RuntimeException("이미 존재하는 카테고리입니다.");
        }
        // 플래너 객체 불러오기
        TeamPlanner planner = teamPlannerRepository.getReferenceById(plannerId);

        // 없다면 객체 생성하여 저장.
        TeamCategory category = TeamCategory.builder()
                .name(dto.getName())
                .color(dto.getColor())
                .teamPlanner(planner)
                .build();

        return categoryRepository.save(category).getId();
    }

    /*
    name : getCategoryList
    function : 카테고리 목록보기,
    GET /api/team-planner/{plannerId}/category
    param : Long plannerId
    return value : List<CategoryDto>
    */

    public List<TeamCategoryDto> getCategories(Long plannerId){


        List<TeamCategory> categories =
                categoryRepository.findByTeamPlanner(
                        teamPlannerRepository.getReferenceById(plannerId)
                );

        List<TeamCategoryDto> dtoList = new ArrayList<>();
        for(TeamCategory t : categories){
            dtoList.add(categoryToDto(t));
        }
        return dtoList;
    }

    /*
    name : updateCategory
    function : 카테고리 수정하기
    PUT /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : CategoryDto
    */

    public TeamCategoryDto updateCategory(Long categoryId, TeamCategoryDto dto){
        //카테고리 불러오기
        TeamCategory category = extractOptionalCategory(categoryId);

        // 필드 수정
        category.setName(dto.getCategoryName());
        category.setColor(dto.getColor());

        // 수정된 객체 저장 및 반환
        return categoryToDto(categoryRepository.save(category));
    }

    /*
    name : deleteCategory
    function : 카테고리 삭제하기
    DELETE /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : x
    */
    public void deleteCategory(Long plannerId, Long categoryId){
        categoryRepository.deleteById(categoryId);
    }


    /*
    name : getScheduleListByCategory
    function : 카테고리별 일정 가져오기
    GET /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : List<TeamScheduleBriefDto
     */

    public List<TeamScheduleBriefDto> getScheduleListByCategory(Long plannerId, Long categoryId){
        // 플래너 객체 가져오기

        // 카테고리 객체 가져오기

        // 일정 목록 가져오기
        List<TeamSchedule> schedules = scheduleRepository.findByTeamPlannerAndCategory(
                extractOptionalPlanner(plannerId),
                extractOptionalCategory(categoryId)
        );
        List<TeamScheduleBriefDto> dtoList = new ArrayList<>();
        for(TeamSchedule t : schedules){
            scheduleToBriefDto(extractOptionalPlanner(plannerId), t);
        }
        return dtoList;
    }


    // ======== private logics ======== //

    private TeamCategoryDto categoryToDto(TeamCategory category){
        TeamCategoryDto dto = TeamCategoryDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .color(category.getColor())
                .build();
        return dto;
    }

    private TeamCategory extractOptionalCategory(Long categoryId){
        if(categoryRepository.findById(categoryId).isPresent()){
            return categoryRepository.findById(categoryId).get();
        }
        else{
            throw new RuntimeException("존재하지 않는 플래너 카테고리입니다.");
        }
    }



    private TeamPlanner extractOptionalPlanner(Long plannerId) throws RuntimeException{
        Optional<TeamPlanner> optionalPlanner = teamPlannerRepository.findById(plannerId);

        if(optionalPlanner.isPresent()){
            return optionalPlanner.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 공유플래너입니다.");
        }
    }

    private TeamScheduleBriefDto scheduleToBriefDto(TeamPlanner planner, TeamSchedule schedule){
        // 생성자 dto 생성

        TeamMemberBriefDto creatorDto = TeamMemberBriefDto.builder()
                .userId(schedule.getCreator().getId())
                .username(schedule.getCreator().getUsername())
                .profileImage(schedule.getCreator().getProfileImageLink())
                .status(teamMemberRepository.findByTeamPlannerAndMember(planner, schedule.getCreator()).getStatus())
                .build();


        // 카테고리가 있다면 dto 생성
        TeamCategoryDto categoryDto = null;
        if(schedule.getCategory() != null){
            categoryDto.setCategoryId(schedule.getCategory().getId());
            categoryDto.setCategoryName(schedule.getCategory().getName());
            categoryDto.setColor(schedule.getCategory().getColor());
        }

        TeamScheduleBriefDto dto = TeamScheduleBriefDto.builder()
                .scheduleId(schedule.getId())
                .creator(creatorDto)
                .title(schedule.getTitle())
                .category(categoryDto)
                .startDateTime(schedule.getStartTime())
                .finishDateTime(schedule.getFinishTime())
                .build();

        return dto;
    }

}
