package GRWM.backend.service.tracker;

import GRWM.backend.dto.tracker.TomorrowMessageCreateDto;
import GRWM.backend.dto.tracker.TomorrowMessageDto;
import GRWM.backend.entity.tracker.TomorrowMessage;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.tracker.TomorrowMessageRepository;
import GRWM.backend.repository.user.MemberRepository;
import GRWM.backend.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TomorrowMessageService {

    private final TomorrowMessageRepository repository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    /*
    name : getTomorrowMessage
    function : 미래 메시지 조회
    URL: GET /api/users/{userId}/future-message/{messageId}
    param: Long userId, Long messagedId
    Response: FutureMessageDto;
    */
    public TomorrowMessageDto getTomorrowMessage(Long userId){
        // 객체 찾기 및 반환
        Optional<TomorrowMessage> messageOptional = repository.findByCreator_IdAndScheduledTimeAfter(userId, LocalDateTime.now());
        if (messageOptional.isPresent()) {
            return messageToDto(messageOptional.get());
        } else {
            return TomorrowMessageDto.builder().build();
        }
    }

    /*
    name : createTomorrowMessage
    function : 미래 메시지 생성
    URL : POST /api/users/{userId}/future-message
   param : Long userId,
Request Body:
{
content: string; // 메시지 내용
LocalDateTime scheduledTime;
}
Response: FutureMessageDto;
    */
    @Transactional
    public TomorrowMessageDto createTomorrowMessage(Long userId, TomorrowMessageCreateDto dto) throws Exception{
        // 객체 생성
        Member member = memberRepository.findById(userId).orElseThrow();
        TomorrowMessage message = TomorrowMessage.builder()
                .creator(member)
                .scheduledTime(dto.getScheduledTime())
                .content(dto.getContent())
                .build();
        TomorrowMessage savedMessage = repository.save(message);

        notificationService.createFutureMessageNotification(member, savedMessage);
        System.out.println("저장 완료!!");
        // 반환
        return messageToDto(savedMessage);
    }

    /*
    name : updateTomorrowMessage
    function : 미래 메시지 수정
    URL: PUT /api/users/{userId}/future-message
    param : Long userId, FutureMessageDto
    Response: FutureMessageDto;
    */
    public TomorrowMessageDto updateTomorrowMessage(Long userId, TomorrowMessageDto dto) throws Exception{
        // 객체 조회
        TomorrowMessage message = repository.findById(dto.getMessageId()).orElseThrow();

        // Notification 객체 조회 및 수정(저장은 호출된 메서드 내에서)
        notificationService.updateNotification(
                notificationService.getNotificationForFutureMessage(
                        userId, message.getId()), dto.getScheduledTime());

        // 메시지 수정
        message.setContent(dto.getContent());
        message.setScheduledTime(dto.getScheduledTime());
        // 저장 및 반환
        return messageToDto(repository.save(message));
    }


    /*
    name : deleteTomorrowMessage
    function : 미래 메시지 삭제
    URL: DELETE /api/users/{userId}/future-message/{messageId}
    param : Long userId, Long messageId
    return value : -
    */
    public void deleteTomorrowMessage(Long userId, Long messageId){
        // 객체 삭제
        repository.deleteById(messageId);

    }

    // ======== private logics ======= //

    private TomorrowMessageDto messageToDto(TomorrowMessage message){
        return TomorrowMessageDto.builder()
                .messageId(message.getId())
                .scheduledTime(message.getScheduledTime())
                .content(message.getContent())
                .build();
    }
}
