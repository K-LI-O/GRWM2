package GRWM.backend.controller.teamplanner;

import GRWM.backend.service.teamplanner.TeamCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamCategoryController {

    private final TeamCategoryService categoryService;

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
    GET /api/team-planner/{plannerId}/category
    param : Long plannerId
    return value : List<CategoryDto>
    */

    /*
    name : updateCategory
    function : 카테고리 수정하기
    PUT /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : CategoryDto
    */

    /*
    name : deleteCategory
    function : 카테고리 삭제하기
    DELETE /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : x
    */

    /*
    name : getScheduleListByCategory
    function : 카테고리별 일정 가져오기
    GET /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : List<TeamScheduleBriefDto

     */
}
