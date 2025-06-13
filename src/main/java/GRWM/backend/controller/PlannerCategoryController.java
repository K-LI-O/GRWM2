package GRWM.backend.controller;


import GRWM.backend.dto.CategoryInfoDto;
import GRWM.backend.dto.PlannerCategoryCreateRequestDto;
import GRWM.backend.service.PlannerCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal-planner/{plannerId}/category")
@RequiredArgsConstructor
public class PlannerCategoryController {

    private final PlannerCategoryService categoryService;

    // 카테고리 추가
    /*
    함수명 : createCategory
    기능 : 플래너에 카테고리를 추가한다.
    매개변수 : Long plannerId, dto; String categoryName, String color;
    반환값 : Long categoryId
     */
    @PostMapping
    public Long createCategory(@PathVariable Long plannerId, @RequestBody PlannerCategoryCreateRequestDto dto){

        return categoryService.createCategory(plannerId, dto);
    }



    // 카테고리 목록 보기
    /*
    함수명 : showCategoryList
    기능 : 플래너의 카테고리 목록 조회
    매개변수 : Long plannerId
    반환값 : List<CategoryInfoDto>
     */

    @GetMapping
    public List<CategoryInfoDto> showCategoryList( @PathVariable Long plannerId){

        return categoryService.showCategoryList(plannerId);
    }


    // 카테고리 edit
    /*
    함수명 : editCategory
    기능 : 이미 존재하는 카테고리의 필드를 수정한다.
    매개변수 : Long plannerId, Long categoryId, dto; String categoryName, String color;
    반환값 : ResponseEntity<Void>
     */

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> editCategory(@PathVariable Long plannerId,
                                             @PathVariable Long categoryId,
                                             @RequestBody PlannerCategoryCreateRequestDto dto){

        try {
            categoryService.editCategory(categoryId, dto);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // 자원을 찾을 수 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 기타 서버 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().build();
        }

    }


    // 카테고리 삭제
    /*
    함수명 : deleteCategory
    기능 : 이미 존재하는 카테고리를 삭제한다.
    매개변수 : Long plannerId, Long categoryId
    반환값 : ResponseEntity<Void>
     */

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long plannerId, @PathVariable Long categoryId){
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // 자원을 찾을 수 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 기타 서버 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().build();
        }
    }



}
