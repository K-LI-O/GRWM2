package GRWM.backend.service.teamplanner;


import GRWM.backend.repository.teamplanner.TeamCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamCategoryService {

    private final TeamCategoryRepository categoryRepository;

        /*
    name : createCategory
    function : 카테고리 생성하기
    POST /api/team-planner/{plannerId}/category
    param : Long plannerId, TeamCategoryCreateDto
    return value : Long categoryId
    */

    /*
    name : getCategoryList
    function : 카테고리 목록보기,
    GET /api/personal-planner/{plannerId}/category
    param : Long plannerId
    return value : List<CategoryDto>
    */

    /*
    name : updateCategory
    function : 카테고리 수정하기
    PUT /api/personal-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : CategoryDto
    */

    /*
    name : deleteCategory
    function : 카테고리 삭제하기
    DELETE /api/pe sonal-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : x
    */

    /*
    name : getScheduleListByCategory
    function : 카테고리별 일정 가져오기
    GET /api/personal-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : List<TeamScheduleBriefDto

     */
}
