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
                .member(extractOptionalUser(userId))
                .fcmToken(token)
                .deviceType("web")
                .build();

        pushTokenRepository.save(pushToken);
    }



    private Member extractOptionalUser(Long userId){


        if(memberRepository.findById(userId).isPresent()){
            return memberRepository.findById(userId).get();
        }
        else{
            throw new RuntimeException(" 존재하지 않는 사용자입니다.");
        }
    }


}
