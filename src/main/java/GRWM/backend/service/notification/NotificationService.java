package GRWM.backend.service.notification;

import GRWM.backend.entity.notification.Notification;
import GRWM.backend.entity.notification.NotificationType;
import GRWM.backend.entity.teamplanner.TeamPlanner;
import GRWM.backend.entity.teamplanner.TimeVote;
import GRWM.backend.entity.tracker.TomorrowMessage;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.NotificationRepository;
import GRWM.backend.repository.user.CommunityUserRepository;

import GRWM.backend.service.PushTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final CommunityUserRepository communityUserRepository;
    private final PushService pushService;
    private final NotificationRepository notificationRepository;
    private final PushTokenService pushTokenService;

    @Transactional // DB 저장 트랜잭션 보장
    public void createFollowNotification(Member member, Long followerId, Long followingId) throws Exception {

        // 1. 알림 내용 생성 (예: 보낸 사람 이름 조회)
        String senderNickname = communityUserRepository.findById(followerId).orElseThrow().getNickname();
        String content = senderNickname + "님이 회원님을 팔로우했습니다.";

        // 2. 알림을 db에 저장.
        Notification not = Notification.builder()
                .receiverId(followingId)
                .senderId(followerId)
                .scheduledTime(Timestamp.from(Instant.now()))
                .type(NotificationType.FOLLOW)
                .title("팔로우 알림")
                .content(content)
                .build();
        notificationRepository.save(not);

        pushService.send(member.getPushToken().getFcmToken(), not.getTitle(), not.getContent(), NotificationType.FOLLOW.toString());


    }

    // 당장 보내
    @Transactional
    public void createTimeVoteNotification(List<Member> members, TeamPlanner planner, TimeVote timeVote) throws Exception{

        // 1. 알림 내용 생성 (예: 보낸 사람 이름 조회)
        String title = planner.getTitle() + " 플래너에 새 시간투표 등록!";
        String content = "시간 투표에 "+ timeVote.getFinishTime() + "까지 투표해주세요.";

        // 2. 알림을 db에 저장.
        for(Member m : members) {
            Notification not = Notification.builder()
                    .receiverId(m.getId())
                    .senderId(m.getId())
                    .type(NotificationType.SCHEDULE)
                    .content(content)
                    .title(title)
                    .build();
            notificationRepository.save(not);

            // 알림 전송
            pushService.send(m.getPushToken().getFcmToken(), not.getTitle(), not.getTitle(),
                    NotificationType.SCHEDULE.toString());
            not.setSent(true);
        }
    }

    // 예약 알림
    @Transactional
    public void createFutureMessageNotification(Member member, TomorrowMessage message) throws Exception {

        // 1. 알림 내용 생성 (예: 보낸 사람 이름 조회)
        String content = message.getContent();

        // 2. 알림을 db에 저장.
        Notification not = Notification.builder()
                .receiverId(member.getId())
                .senderId(member.getId())
                .type(NotificationType.FOR_ME_TOMORROW)
                .messageId(message.getId())
                .content(content)
                .build();
        System.out.println("저장 완료.");
        System.out.println(notificationRepository.save(not).getMessageId());

    }

    @Scheduled(cron = "0 * * * * *")
    protected void sendNotifications() throws Exception {
        List<Notification> notifications = notificationRepository.findByIsSentFalseAndScheduledTimeBefore(Timestamp.from(Instant.now()));

        for(Notification n : notifications) {
           pushService.send(pushTokenService.getToken(n.getReceiverId()),
                   n.getTitle(), n.getContent(), n.getType().toString());
            n.setSent(true);
            notificationRepository.save(n);

        }
    }

    // 내일 메시지 수정용 어쩌구
    @Transactional(readOnly = true)
    public Notification getNotificationForFutureMessage(Long userId, Long messageId){
        return notificationRepository.findByReceiverIdAndMessageId(
                userId,
                messageId
                ).orElseThrow();
    }

    @Transactional
    public void updateNotification(Notification not, LocalDateTime updatedTime){
        not.setScheduledTime(Timestamp.valueOf(updatedTime));
        notificationRepository.saveAndFlush(not);
    }


}
