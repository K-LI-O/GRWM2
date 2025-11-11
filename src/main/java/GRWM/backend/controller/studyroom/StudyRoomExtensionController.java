package GRWM.backend.controller.studyroom;

import GRWM.backend.dto.studyroom.ExtensionDto;
import GRWM.backend.dto.studyroom.ExtensionVoteDto;
import GRWM.backend.dto.studyroom.VoteDto;
import GRWM.backend.service.studyroom.StudyRoomExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.Extension;

@RestController
@RequiredArgsConstructor
public class StudyRoomExtensionController {

    private final StudyRoomExtensionService service;

    /*
    name : extensionVote
    function : 연장 투표
    URL: POST /api/study-rooms/{studyRoomId}/extension-vote
    Request: Long studyRoomId
{
vote: "agree" | "disagree"; // 찬성/반대
}
    Response: ExtensionVoteDto
    */
    @PostMapping("/api/study-rooms/{studyRoomId}/extension-vote")
    public ExtensionVoteDto extensionVote(@PathVariable Long studyRoomId,
                                          @RequestBody VoteDto dto){
        return service.extensionVote(studyRoomId, dto);
    }

        /*
    name : extendStudyRoom
    function: 스터디룸 연장
    URL: POST /api/study-rooms/{studyRoomId}/extend
    Request: Long studyRoomId
    Response:
    {
    LocalTime extendedEndTime; 연장된 종료 시간
    int extendedCount; 지금까지 연장한 횟수
    }
    */
    @PostMapping("/api/study-rooms/{studyRoomId}/extend")
    public ExtensionDto extendStudyRoom(@PathVariable Long studyRoomId){
        return service.extendStudyRoom(studyRoomId);
    }

    /*
    name : finishStudyRoom
    function : 스터디룸 자동 종료
    URL: POST /api/study-rooms/{studyRoomId}/close
    Request: Long studyRoomId
    * 이것도 스케줄러를 이용해야할 것 같음.
     */
    @PostMapping("/api/study-rooms/{studyRoomId}/close")
    public Long finishStudyRoom(@PathVariable Long studyRoomId){
        return service.finishStudyRoom(studyRoomId);
    }
}
