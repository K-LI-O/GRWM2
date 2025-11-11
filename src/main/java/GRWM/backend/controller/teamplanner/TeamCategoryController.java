package GRWM.backend.controller.teamplanner;


import GRWM.backend.dto.teamPlanner.TeamCategoryCreateDto;
import GRWM.backend.dto.teamPlanner.TeamCategoryDto;
import GRWM.backend.dto.teamPlanner.TeamScheduleBriefDto;
import GRWM.backend.service.teamplanner.TeamCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/api/team-planner/{plannerId}/category")
    public Long createCategory(@PathVariable Long plannerId, @RequestBody TeamCategoryCreateDto dto){
        return categoryService.createCategory(plannerId, dto);
    }

    /*
    name : getCategoryList
    function : 카테고리 목록보기,
    GET /api/team-planner/{plannerId}/category
    param : Long plannerId
    return value : List<CategoryDto>
    */
    @GetMapping("/api/team-planner/{plannerId}/category")
    public List<TeamCategoryDto> getCategoryList(@PathVariable Long plannerId){
        return categoryService.getCategories(plannerId);
    }

    /*
    name : updateCategory
    function : 카테고리 수정하기
    PUT /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : CategoryDto
    */
    @PutMapping("/api/team-planner/{plannerId}/category/{categoryId}")
    public TeamCategoryDto updateCategory(@PathVariable Long plannerId, @PathVariable Long categoryId, @RequestBody TeamCategoryDto dto){
        return categoryService.updateCategory(plannerId, dto);
    }

    /*
    name : deleteCategory
    function : 카테고리 삭제하기
    DELETE /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : x
    */
    @DeleteMapping("/api/team-planner/{plannerId}/category/{categoryId}")
    public void deleteCategory(@PathVariable Long plannerId, @PathVariable Long categoryId){
        categoryService.deleteCategory(plannerId, categoryId);
    }

    /*
    name : getScheduleListByCategory
    function : 카테고리별 일정 가져오기
    GET /api/team-planner/{plannerId}/category/{categoryId}
    param : Long plannerId, Long categoryId
    return value : List<TeamScheduleBriefDto>
     */
    @GetMapping("/api/team-planner/{plannerId}/category/{categoryId}")
    public List<TeamScheduleBriefDto> getScheduleListByCategory(@PathVariable Long plannerId,
                                                                @PathVariable Long categoryId){
        return categoryService.getScheduleListByCategory(plannerId, categoryId);
    }
}
