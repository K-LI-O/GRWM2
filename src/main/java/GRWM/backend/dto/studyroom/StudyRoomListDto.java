package GRWM.backend.dto.studyroom;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomListDto {
    List<StudyRoomBriefDto> studyRooms;
    int totalElement; // 스터디룸의 총 개수
    int currentPage; // 페이징 시 현재 보내는 페이지
    int totalPages; // 전체 페이지 개수 (페이징 관련 파라미터)

}
