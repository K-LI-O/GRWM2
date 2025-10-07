package GRWM.backend.service.notification;

import GRWM.backend.entity.Notification;
import GRWM.backend.entity.NotificationType;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.NotificationRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CommunityUserRepository communityUserRepository;
    @Transactional // DB 저장 트랜잭션 보장
    public void createFollowNotification(Long followerId, Long followingId) {

        // 1. 알림 내용 생성 (예: 보낸 사람 이름 조회)
        String senderNickname = extractOptionalUser(followerId).getNickname();
        String content = senderNickname + "님이 회원님을 팔로우했습니다.";

        // 2. Notification 엔티티 생성 및 DB에 저장
        Notification notification = Notification.builder()
                .receiverId(followingId) // 알림 받는 사람
                .senderId(followerId)   // 알림 보낸 사람
                .type(NotificationType.FOLLOW)
                .content(content)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        // 3. (선택 사항) 실시간 푸시 알림 전송
        // 푸시 서비스는 알림을 즉시 사용자 기기로 전송하는 역할을 합니다.
        // 이는 웹소켓이나 Firebase Cloud Messaging (FCM) 등을 통해 구현됩니다.
        // pushService.send(followingId, content);
    }




    private CommunityUser extractOptionalUser(Long communityId){
        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);
        CommunityUser user = null;


        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }
}
