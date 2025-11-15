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
        Member member = memberRepository.findById(userId).orElseThrow();
        PushToken existingToken = pushTokenRepository.findByMemberAndDeviceType(
                member, "web");


        if (existingToken != null) {
            // 2. 기존 토큰이 있다면 새 토큰으로 업데이트 (UPDATE)
            if (!existingToken.getFcmToken().equals(token)) {
                existingToken.setFcmToken(token);
                pushTokenRepository.save(existingToken);
            }
        } else {
            // 3. 기존 토큰이 없다면 새로 생성 (INSERT)
            PushToken newPushToken = PushToken.builder()
                    .member(member)
                    .fcmToken(token)
                    .build();
            pushTokenRepository.save(newPushToken);
        }
        System.out.println(token+"\n");

    }

    public String getToken(Long userId){
        return pushTokenRepository.findByMember_Id(userId).getFcmToken();

    }




}
