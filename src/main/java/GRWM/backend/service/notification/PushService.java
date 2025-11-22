package GRWM.backend.service.notification;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PushService {

    public void send(String token,
                                 String title, String body, String type) throws Exception{

        Notification not = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(not)
                .putData("type", type)
                .build();

        FirebaseMessaging.getInstance().send(message);
    }

    public void sendMulticast(List<String> tokens, String title, String body, String type){
        // 1. Firebase 알림 객체 생성
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        // 2. MulticastMessage 객체 생성
        // 8명 정도의 토큰은 500개 제한에 걸리지 않으므로 바로 리스트를 사용합니다.
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .setNotification(notification)
                .putAllData(Map.of("key", "value")) // 선택 사항: 추가 데이터 필드
                .putData("type", type)
                .addAllTokens(tokens) // 타겟 토큰 목록 추가
                .build();

        // 3. 알림 전송 및 결과 처리
        try {
            // FCM에 한 번의 요청으로 전송
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(multicastMessage);

            // 전송 실패 토큰 처리 (DB에서 삭제 등 클린업 로직 추가 필요)
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();

                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        // 실패한 토큰을 모아 DB에서 삭제하는 로직을 여기에 추가
                        failedTokens.add(tokens.get(i));
                        System.err.println("알림 전송 실패: " + responses.get(i).getException().getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("FCM 전송 중 오류 발생: " + e.getMessage());
        }
    }


    public void handleInvalidTokens(List<String> invalidTokens){

    }

}
