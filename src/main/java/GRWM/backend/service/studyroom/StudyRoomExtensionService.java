package GRWM.backend.service.studyroom;

import GRWM.backend.dto.studyroom.ExtensionDto;
import GRWM.backend.dto.studyroom.ExtensionVoteDto;
import GRWM.backend.dto.studyroom.VoteDto;
import GRWM.backend.entity.studyroom.StudyRoom;
import GRWM.backend.repository.studyroom.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRoomExtensionService {

    private final TaskScheduler taskScheduler;
    private final StudyRoomRepository studyRoomRepository;
    private final SimpMessagingTemplate messagingTemplate; // 메시지 전파 도구
    /*
    name : sendExtensionNotification
    function : 연장(을 위한?) 알림 발송
    URL: POST /api/study-rooms/{studyRoomId}/extension-notification
    Request: Long studyRoomId
    Response:
{
Long notificationId
participants: User[]; ? }
* 이건 스케줄러를 이용하여 백에서 웹 푸시를 보내야 하는 기능같음. 아니면 웹소켓…..
    */
    public void extensionNotification(Long studyRoomId) {
        // 1. DB 처리: 스터디룸 정보 저장
        StudyRoom room = studyRoomRepository.findById(studyRoomId).orElseThrow();

        // 2. 알림 발송 시점 계산 (예: 만료 10분 전 연장 투표 알림)
        LocalDateTime notificationDateTime = room.getCreatedAt().plusMinutes(room.getDuration() - 10);

        // 3. LocalDateTime을 TaskScheduler가 요구하는 java.util.Date 객체로 변환
        Instant instant = notificationDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date scheduledDate = Date.from(instant);

        // 4. TaskScheduler를 사용하여 예약 작업 실행
        taskScheduler.schedule(() -> {
            // 💡 예약된 시간에 실행될 핵심 로직 (알림 발송 Service 메서드 호출)
            System.out.println(studyRoomId + " 스터디룸 연장 만료 10분 전 연장투표 알림 실행");
            // notificationService.sendExtensionExpirationNotice(roomId);

            // 웹소켓/웹푸시 발송 로직은 이 람다식(Lambda) 내에서,
            // 웹소켓으로 전파
            String destination =  "/topic/studyroom." + studyRoomId + ".extension";
            messagingTemplate.convertAndSend(destination, "스터디룸 연장 투표해주세요.");

        }, instant);
    }


    /*
    name : extensionVote
    function : 연장 투표
    URL: POST /api/study-rooms/{studyRoomId}/extension-vote
    Request: Long studyRoomId
{
vote: "agree" | "disagree"; // 찬성/반대
}
    Response:
{
int agreeCount; (찬성 투표자 수)
int votedCount; (투표자 수)
int totalParticipants; (전체 참여자 수)
boolean isCompleted; (투표가 완료되었는지의 여부)
boolean result; (결과; 투표가 완료되기 전에는 사용하지 말 것)
}
    */
    public ExtensionVoteDto extensionVote(Long studyRoomId, VoteDto dto){
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);
        if(dto.getVote().equals("agree")) {
            studyRoom.setAgreedCount(studyRoom.getAgreedCount() + 1);
            studyRoom.setVoteCount(studyRoom.getVoteCount() + 1);
            studyRoomRepository.save(studyRoom);
        }

        ExtensionVoteDto result = ExtensionVoteDto.builder()
                .agreeCount(studyRoom.getAgreedCount())
                .votedCount(studyRoom.getVoteCount())
                .totalParticipants(studyRoom.getMemberCount())
                .isCompleted(((double) studyRoom.getVoteCount() / studyRoom.getMemberCount() >= (double) 2 / 3))
                .result(voteCompletion(
                        studyRoom.getAgreedCount(),
                        studyRoom.getVoteCount(),
                        studyRoom.getMemberCount())
                )
                .build();
        return result;
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

    public ExtensionDto extendStudyRoom(Long studyRoomId) {
        // 1. DB 처리: 스터디룸 정보를 newEndTime으로 업데이트 및 저장
        StudyRoom room = studyRoomRepository.findById(studyRoomId).orElseThrow();
        room.setExtensionCount(1);
        studyRoomRepository.save(room);

        // 2. 알림 발송 시점 계산 (예: 만료 1분 전 연장)
        LocalDateTime notificationDateTime = room.getCreatedAt().plusMinutes(room.getDuration() - 1);

        // 3. LocalDateTime을 TaskScheduler가 요구하는 java.util.Date 객체로 변환
        Instant instant = notificationDateTime.atZone(ZoneId.systemDefault()).toInstant();

        // 4. TaskScheduler를 사용하여 예약 작업 실행
        taskScheduler.schedule(() -> {
            // 예약된 시간에 실행될 핵심 로직(스터디룸 연장)
            // 웹소켓/웹푸시 발송 로직은 이 람다식(Lambda) 내에서,
            room.setExtensionTime(room.getExtensionTime());
            studyRoomRepository.save(room);
            // 웹소켓으로 전파
            String destination =  "/topic/studyroom." + studyRoomId + ".extension";
            messagingTemplate.convertAndSend(destination, "스터디룸 시간이 " +room.getExtensionTime()+ "분 연장되었습니다.");

        }, instant);
        return new ExtensionDto(
                room.getCreatedAt().toLocalTime().plusMinutes(
                        room.getDuration() + room.getExtensionTime()), 1);
    }


    /*
    name : finishStudyRoom
    function : 스터디룸 자동 종료
    URL: POST /api/study-rooms/{studyRoomId}/close
    Request: Long studyRoomId
    * 이것도 스케줄러를 이용해야할 것 같음.
     */

    public Long finishStudyRoom(Long studyRoomId) {
        // 1. DB 처리: 스터디룸 정보를 newEndTime으로 업데이트 및 저장
        StudyRoom room = studyRoomRepository.findById(studyRoomId).orElseThrow();

        // 2. 알림 발송 시점 계산 (예: 만료 5분 전 알림)
        int range = room.getExtensionCount() == 1 ? room.getDuration() + room.getExtensionTime() : room.getDuration();
        LocalDateTime notificationDateTime = room.getCreatedAt().plusMinutes(range);

        // 3. LocalDateTime을 TaskScheduler가 요구하는 java.util.Date 객체로 변환
        Instant instant = notificationDateTime.atZone(ZoneId.systemDefault()).toInstant();

        // 4. TaskScheduler를 사용하여 예약 작업 실행
        taskScheduler.schedule(() -> {
            // 예약된 시간에 실행될 핵심 로직 (알림 발송 Service 메서드 호출)
            room.setActive(false);
            studyRoomRepository.save(room);
            // 웹소켓으로 전파
            String destination = "/topic/studyroom." + studyRoomId + ".extension";
            messagingTemplate.convertAndSend(destination, "ROOM_CLOSED");
        }, instant);
        return room.getId();
    }


    // ======= private logics ======= //

    private StudyRoom extractOptionalRoom(Long studyRoomId){
        Optional<StudyRoom> studyRoom = studyRoomRepository.findById(studyRoomId);
        return studyRoom.orElseThrow();
    }

    private boolean voteCompletion(int agreedCount, int voteCount, int memberCount){
        if(voteCount==memberCount && (double)agreedCount/memberCount >= 0.5) return true;
        if((double) voteCount/memberCount >= 0.5 && (double)agreedCount/memberCount >= (double)2/3) return true;
        return false;
    }

}
