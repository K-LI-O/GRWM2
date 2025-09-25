package GRWM.backend.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomManager {

    @Autowired
    private AmqpAdmin amqpAdmin;

    // Exchange와 Queue 바인딩
    public void createSetting(Long chatRoomId){

        String exchangeName = "chat." + chatRoomId;
        String queueName = "Queue.chat."+chatRoomId;


        amqpAdmin.declareExchange(new TopicExchange(exchangeName, true, false));
        amqpAdmin.declareQueue(new Queue(queueName, true, false, false));
        amqpAdmin.declareBinding(BindingBuilder.bind(new Queue(queueName)).to(new TopicExchange(exchangeName)).with("#"));
    }



}
