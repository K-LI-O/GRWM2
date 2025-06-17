package GRWM.backend.service;

import GRWM.backend.dto.personalPlanner.CategoryInfoDto;
import GRWM.backend.dto.personalPlanner.PlannerCategoryCreateRequestDto;
import GRWM.backend.entity.PersonalPlanner;
import GRWM.backend.entity.PlannerCategory;
import GRWM.backend.repository.PersonalPlannerRepository;
import GRWM.backend.repository.PlannerCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlannerCategoryService {

    private final PersonalPlannerRepository ppRepository;
    private final PlannerCategoryRepository categoryRepository;

   /*
    함수명 : createCategory
    기능 : 카테고리를 생성하고 저장한다.
    매개변수 : Long plannerId, dto; String categoryName, String color;
    반환값 : Long categoryId
     */

    public Long createCategory(Long plannerId, PlannerCategoryCreateRequestDto dto){

        // 플래너 불러오기
        PersonalPlanner planner = ppRepository.getReferenceById(plannerId);

        // 카테고리 생성하기
        PlannerCategory category = new PlannerCategory(dto.getName(), dto.getColor(), planner);

        // 카테고리 저장하고 아이디 반환
        PlannerCategory savedCategory = categoryRepository.save(category);

        return savedCategory.getId();
    }


    /*
    함수명 : showCategoryList
    기능 : 플래너의 카테고리 목록 조회
    매개변수 : Long plannerId, dto; String categoryName, String color;
    반환값 : List<CategoryInfoDto>
     */

    @Transactional(readOnly = true)
    public List<CategoryInfoDto> showCategoryList(Long plannerId){

        // 플래너 찾기
        PersonalPlanner pp = ppRepository.getReferenceById(plannerId);
        // 카테고리 객체 리스트 조회하기
        List<PlannerCategory> categoryList = categoryRepository.findByPersonalPlanner(pp);
        List<CategoryInfoDto> dtoList = new ArrayList<>();


        // 카테고리정보 dto 로 트랜지션
        for(PlannerCategory category : categoryList){
            CategoryInfoDto dto = new CategoryInfoDto(category.getId(), category.getName(), category.getColor()
            );
            dtoList.add(dto);
        }

        return dtoList;
    }

    /*
    함수명 : editCategory
    기능 : 이미 존재하는 카테고리의 필드를 수정한다.
    매개변수 : Long plannerId, Long categoryId, dto; String categoryName, String color;
    반환값 : void
     */


    public void editCategory(Long categoryId, PlannerCategoryCreateRequestDto dto){

        // 카테고리 객체 가져오기
        Optional<PlannerCategory> optionalCategory = categoryRepository.findById(categoryId);
        PlannerCategory category;

        if(optionalCategory.isPresent()){
            category = optionalCategory.get();
        } else {
            // 객체를 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
        }

        // 카테고리 정보 수정하기
        category.setName(dto.getName());
        category.setColor(dto.getColor());

    }


    // 카테고리 삭제
    /*
    함수명 : deleteCategory
    기능 : 이미 존재하는 카테고리를 삭제한다.
    매개변수 : Long categoryId
    반환값 : void
     */

    public void deleteCategory(Long categoryId){

        // 카테고리 객체 가져오기
        Optional<PlannerCategory> optionalCategory = categoryRepository.findById(categoryId);
        PlannerCategory category;

        if(optionalCategory.isPresent()){
            category = optionalCategory.get();
        } else {
            // 카테고리를 찾지 못했을 때의 로직
            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
        }
        categoryRepository.delete(category);
    }



}
