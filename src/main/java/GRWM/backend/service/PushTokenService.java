package GRWM.backend.service;

import GRWM.backend.entity.notification.PushToken;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.PushTokenRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushTokenService {

    private final PushTokenRepository pushTokenRepository;
    private final MemberRepository memberRepository;

    public void saveToken(Long userId, String token){

        // 사용자 불러오기,

        PushToken pushToken = PushToken.builder()
                .member(memberRepository.findById(userId).orElseThrow())
                .fcmToken(token)
                .deviceType("web")
                .build();

        pushTokenRepository.save(pushToken);
    }

    public String getToken(Long userId){
        return pushTokenRepository.findByMember_Id(userId).getFcmToken();

    }




}
