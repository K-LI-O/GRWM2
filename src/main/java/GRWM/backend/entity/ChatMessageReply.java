package GRWM.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // ChatMessage의 모든 변수들을 포함하는 생성자;
public class ChatMessageReply extends ChatMessage {

    private Long replyTo;
}
