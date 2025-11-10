package GRWM.backend.controller.tracker;

import GRWM.backend.dto.tracker.DiaryCreateDto;
import GRWM.backend.dto.tracker.DiaryDto;
import GRWM.backend.dto.tracker.DiaryListDto;
import GRWM.backend.service.tracker.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /*
    name : getDiaryList
    function : 일기 목록 조회
    URL: GET /api/users/{userId}/diaries
    param : Long userId
Query Parameters:
{
Date?: string; // 날짜
category?: string; // 카테고리
emotion?: "happy" | "sad" | ...; // 감정
keyword?: string; // 검색 키워드
page?: number; // 페이지 번호
limit?: number; // 페이지당 개수 }
Response:
{
Diary diaries;
int totalCount;
int currentPage;
int totalPages;
}
+ 예시로 쓰고 있는 감정들... 좀 더 정리해도 될 듯!
희로애락만 한다거나  기쁨  평온  생각  우울  즐거움  슬픔 –
*/
    @GetMapping("/api/users/{userId}/diaries")
    public DiaryListDto getDiaryList(@PathVariable Long userId,
                                     @RequestParam LocalDate date,
                                     @RequestParam String category,
                                     @RequestParam String emotion,
                                     @RequestParam String keyword,
                                     @RequestParam int page,
                                     @RequestParam int limit){
        return diaryService.getDiaryList(userId, date, category, emotion, keyword, page, limit);
    }

/*
    name : getDiaryDetail
    function : 일기 상세 조회
    URL: GET /api/users/{userId}/diaries/{diaryId}
    param : Long userId, Long diaryId
    return value : DiaryDto
    */
    @GetMapping("/api/users/{userId}/diaries/{diaryId}")
    public DiaryDto getDiaryDetail(@PathVariable Long userId,
                                   @PathVariable Long diaryId){
        return diaryService.getDiaryDetail(userId, diaryId);
    }

    /*
    name : createDiary
    function : 일기 작성
    URL: POST /api/users/{userId}/diaries
    param : Long userId, DiaryCreateDto
    return value : DiaryDto
    */
    @PostMapping("/api/users/{userId}/diaries")
    public DiaryDto createDiary(@PathVariable Long userId,
                                @RequestBody DiaryCreateDto dto){
        return diaryService.createDiary(userId,dto);
    }

    /*
    name : updateDiary
    function : 일기 수정
    URL: PUT /api/users/{userId}/diaries/{diaryId}
    param: Long userId, Long diaryId
    RequestBody: DiaryDto
    return value : DiaryDto
    */
    @PutMapping("/api/users/{userId}/diaries/{diaryId}")
    public DiaryDto updateDiary(@PathVariable Long userId,
                                @PathVariable Long diaryId,
                                @RequestBody DiaryDto dto){
        return diaryService.updateDiary(userId, diaryId, dto);
    }


    /*
    name : deleteDiary
    function : 일기 삭제
    URL: DELETE /api/users/{userId}/diaries/{diaryId}
    param : Long userId, Long diaryId
    return value : no content 204
     */
    @DeleteMapping("/api/users/{userId}/diaries/{diaryId}")
    public void deleteDiary(@PathVariable Long userId,
                            @PathVariable Long diaryId){
        diaryService.deleteDiary(userId, diaryId);
    }


}
