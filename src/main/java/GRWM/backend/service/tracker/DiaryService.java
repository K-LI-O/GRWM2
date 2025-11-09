package GRWM.backend.service.tracker;

import GRWM.backend.dto.tracker.DiaryCreateDto;
import GRWM.backend.dto.tracker.DiaryDto;
import GRWM.backend.dto.tracker.DiaryListDto;
import GRWM.backend.entity.tracker.Diary;
import GRWM.backend.entity.tracker.Emotion;
import GRWM.backend.repository.tracker.DiaryRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

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
희로애락만 한다거나  기쁨(Happy)  평온(Relieved)  생각(Thinking)  우울(Depressed)  분노(Angry)  슬픔(Sad) –

*/
    public DiaryListDto getDiaryList(LocalDate date, String category, String emotion, String keyword,
                                     int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Diary> diaryPage = diaryRepository.findByDateAndCategoryAndEmotionAndKeyword(date, category, Emotion.valueOf(emotion), keyword, pageable);
        List<Diary> diaryList = diaryPage.getContent();
        List<DiaryDto> dtoList = new ArrayList<>();
        if(diaryList.isEmpty())
            return new DiaryListDto(dtoList, diaryPage.getTotalElements(),
                diaryPage.getNumber(), diaryPage.getTotalPages());
        for(Diary d : diaryList){
            DiaryDto dto = DiaryDto.builder()
                    .id(d.getId())
                    .createdAt(d.getCreateAt())
                    .updatedAt(d.getUpdatedAt())
                    .category(d.getCategory())
                    .title(d.getTitle())
                    .content(d.getContent())
                    .tags(d.getTags())
                    .emotion(d.getEmotion().toString())
                    .build();
            dtoList.add(dto);
        }

        return new DiaryListDto(dtoList, diaryPage.getTotalElements(),
                diaryPage.getNumber(), diaryPage.getTotalPages());

    }

/*
    name : getDiaryDetail
    function : 일기 상세 조회
    URL: GET /api/users/{userId}/diaries/{diaryId}
    param : Long userId, Long diaryId
    return value : DiaryDto
*/

    public DiaryDto getDiaryDetail(Long userId, Long diaryId){
        // 리포지토리에서 조회
         Diary diary = diaryRepository.findById(diaryId).orElseThrow();
        // 반환
        return diaryToDto(diary);
    }


    /*
    name : createDiary
    function : 일기 작성
    URL: POST /api/users/{userId}/diaries
    param : Long userId, DiaryCreateDto
    return value : DiaryDto
    */
    public DiaryDto createDiary(Long userId, DiaryCreateDto dto){
        // 새 객체 생성
        Diary diary = Diary.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(memberRepository.findById(userId).orElseThrow())
                .category(dto.getCategory())
                // .emotion(dto.getEmotion())
                .tags(dto.getTags())
                .date(dto.getDate())
                .build();
        Diary savedDiary = diaryRepository.save(diary);
        // 저장

        // 반환
        return diaryToDto(savedDiary);
    }

    /*
    name : updateDiary
    function : 일기 수정
    URL: PUT /api/users/{userId}/diaries/{diaryId}
    param: Long userId, Long diaryId
    RequestBody: DiaryDto
    return value : DiaryDto
    */
    public DiaryDto updateDiary(Long userId, Long diaryId, DiaryDto dto){
        // 객체 불러오기
        Diary diary = diaryRepository.findById(diaryId).orElseThrow();

        // 수정하기
        diary.setCategory(dto.getCategory());
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setTags(dto.getTags());
        //diary.setEmotion();

        // 저장하기
        Diary savedDiary = diaryRepository.save(diary);
        return diaryToDto(savedDiary);
    }


    /*
    name : deleteDiary
    function : 일기 삭제
    URL: DELETE /api/users/{userId}/diaries/{diaryId}
    param : Long userId, Long diaryId
    return value : no content 204
     */
    public void deleteDiary(Long userId, Long diaryId){
        diaryRepository.deleteById(diaryId);
    }

    // ======= private logics ======= //

    private DiaryDto diaryToDto(Diary diary){
        // dto 담기
        DiaryDto result = DiaryDto.builder()
                .id(diary.getId())
                .createdAt(diary.getCreateAt())
                .category(diary.getCategory())
                .title(diary.getTitle())
                .content(diary.getContent())
                .tags(diary.getTags())
                .emotion(diary.getEmotion().toString())
                .build();
        return result;
    }

}
