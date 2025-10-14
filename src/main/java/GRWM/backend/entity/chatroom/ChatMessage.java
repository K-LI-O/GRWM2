package GRWM.backend.entity.chatroom;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    protected Long id;
    protected Long memberId;

    protected Long replyMessageId;
    protected MessageType type;

    protected String content;

    @CreatedDate
    protected LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    protected ChatRoom chatRoom;

    protected String writerChatName;


    public ChatMessage(Long memberId, Long replyMessageId, String content, int type, ChatRoom chatRoom, String writerChatName){
        this.memberId = memberId; // communityId
        this.replyMessageId = replyMessageId;
        this.content = content;
        this.type = castingIntToEnum(type);
        this.chatRoom = chatRoom;
        this.writerChatName = writerChatName;
    }

    /**
     * name : castingIntToEnum
     * functionality : cast an int variable to enum
     * param : int code
     * return : MessageType
     */
    private MessageType castingIntToEnum(int code) {
        for (MessageType type : MessageType.values()) {

            if (type.ordinal() == code) {
                return type;
            }
        }
        // Handle invalid code:
        throw new IllegalArgumentException("Invalid status code: " + code);
    }




}
