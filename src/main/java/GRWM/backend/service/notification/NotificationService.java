package GRWM.backend.service.notification;

import GRWM.backend.entity.notification.Notification;
import GRWM.backend.entity.notification.NotificationType;
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

    private final CommunityUserRepository communityUserRepository;
    private final PushService pushService;
    private final NotificationRepository notificationRepository;

    @Transactional // DB 저장 트랜잭션 보장
    public void createFollowNotification(Long followerId, Long followingId) {

        // 1. 알림 내용 생성 (예: 보낸 사람 이름 조회)
        String senderNickname = extractOptionalUser(followerId).getNickname();
        String content = senderNickname + "님이 회원님을 팔로우했습니다.";

        // 2. 알림을 db에 저장.
        Notification not = Notification.builder()
                .receiverId(followingId)
                .senderId(followerId)
                .type(NotificationType.FOLLOW)
                .content(content)
                .build();
        notificationRepository.save(not);


        // 3. (선택 사항) 실시간 푸시 알림 전송
        // 푸시 서비스는 알림을 즉시 사용자 기기로 전송하는 역할을 합니다.
        // 이는 웹소켓이나 Firebase Cloud Messaging (FCM) 등을 통해 구현됩니다.
        //pushService.send(token, "팔로우 알림", content, not.getType());
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
