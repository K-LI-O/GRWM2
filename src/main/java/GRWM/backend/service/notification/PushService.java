package GRWM.backend.service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void sendMulticast(List<String> tokens, String title, String body){

    }

    public void handleInvalidTokens(List<String> invalidTokens){

    }

}
