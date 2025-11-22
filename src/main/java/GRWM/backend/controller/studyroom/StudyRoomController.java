package GRWM.backend.controller.studyroom;

import GRWM.backend.dto.StudyRoomJoinDto;
import GRWM.backend.dto.studyroom.*;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.studyroom.StudyRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StudyRoomController {

    private final StudyRoomService studyRoomService;
    /*
    name : createStudyRoom
    URL: POST /api/study-rooms
    param : StudyRoomCreateDto, UserDetails
    return value : Long studyRoomId
    */
    @PostMapping("/api/study-rooms")
    public Long createStudyRoom(@RequestBody StudyRoomCreateDto dto,
                           @AuthenticationPrincipal CustomUserDetails userDetails){
        return studyRoomService.createStudyRoom(dto, userDetails.getCommunityUserId());
    }


    /*
    name : getStudyRoomList
    URL: GET /api/study-rooms
    param : int page, int limit(request param)
    return value :  StudyRoomListDto
{
List<StudyRoomBriefDto> studyRooms;
int totalElement; 스터디룸의 총 개수
int currentPage; 페이징 시 현재 보내는 페이지
int totalPages; 전체 페이지 개수 (페이징 관련 파라미터)
}
    */
    @GetMapping("/api/study-rooms")
    public StudyRoomListDto getStudyRoomList(@RequestParam int page, @RequestParam int limit){
        return studyRoomService.getStudyRoomList(page, limit);
    }


    /*
    name : getStudyRoomDetail
    URL: GET /api/study-rooms/{studyRoomId}
    param : Long studyRoomId
    return value : studyRoomDetailDto
{
StudyRoomDto studyRoom;
currentUserStatus: "joined" | "owner";
}
    */
    @GetMapping("/api/study-rooms/{studyRoomId}")
    public StudyRoomDetailDto getStudyRoomDetail(@PathVariable Long studyRoomId,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        return studyRoomService.getStudyRoomDetail(studyRoomId, userDetails.getCommunityUserId());
    }

    /*
    name : joinStudyRoom
    URL: POST /api/study-rooms/{studyRoomId}/join
    param : Long studyRoomId;
    return value : ResponseEntity<Boolean>
    */
    @PostMapping("/api/study-rooms/{studyRoomId}/join")
    public ResponseEntity<Boolean> joinStudyRoom(@PathVariable Long studyRoomId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody StudyRoomJoinDto dto){
        return ResponseEntity.ok(studyRoomService.joinStudyRoom(studyRoomId, userDetails.getCommunityUserId(), dto));
    }

    /*
    name : GoOutStudyRoom
    URL: POST /api/study-rooms/{studyRoomId}/leave
    param : Long studyRoomId;
    return value : ResponseEntity<Boolean>
     */
    @PostMapping("/api/study-rooms/{studyRoomId}/leave")
    public ResponseEntity<Boolean> goOutStudyRoom(@PathVariable Long studyRoomId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(studyRoomService.goOutStudyRoom(studyRoomId, userDetails.getCommunityUserId()));
    }



        /*
    name : findActivatedStudyRoom
    URL: GET /api/study-rooms/joined
    return value :  StudyRoomBriefDto
    */
    @GetMapping("/api/study-rooms/joined")
    public StudyRoomBriefDto findActivatedStudyRoom(@AuthenticationPrincipal CustomUserDetails userDetails){
        return studyRoomService.findActivatedStudyRoom(userDetails.getCommunityUserId());
    }


}
