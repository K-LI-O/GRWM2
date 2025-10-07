package GRWM.backend.service.notification;

import GRWM.backend.repository.NotificationRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushService {

    private final NotificationService notificationService;

    public void sendNotification(String token,
                                 String title,
                                 String body,
                                 GRWM.backend.entity.Notification notification) throws Exception{

        Notification not = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(not)
                .putData("type", notification.getType().toString())
                .putData("senderId", String.valueOf(notification.getSenderId()))
                .build();

        FirebaseMessaging.getInstance().send(message);
    }

    public void sendMulticast(List<String> tokens, String title, String body){

    }

    public void handleInvalidTokens(List<String> invalidTokens){

    }

}
