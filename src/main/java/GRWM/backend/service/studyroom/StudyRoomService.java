package GRWM.backend.service.studyroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRoomService {
    /*
    name : createStudyRoom
    URL: POST /api/study-rooms
    param : StudyRoomCreateDto, UserDetails
    return value : Long studyRoomId
    */


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

    /*
    name : joinStudyRoom
    URL: POST /api/study-rooms/{studyRoomId}/join
    param : Long studyRoomId;
    return value : ResponseEntity<Boolean>

    */

    /*
    name : GoOutStudyRoom
    URL: POST /api/study-rooms/{studyRoomId}/leave
    param : Long studyRoomId;
    return value : ResponseEntity<Boolean>
     */
}
