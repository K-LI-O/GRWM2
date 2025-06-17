package GRWM.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트에게 메시지를 발행할 때 사용할 prefix
        // "/topic"으로 시작하는 메시지는 메시지 브로커가 처리하여 구독자에게 전달
        config.enableSimpleBroker("/topic");

        // 애플리케이션으로 들어오는 메시지의 destination prefix
        // "/app"으로 시작하는 메시지는 @MessageMapping 어노테이션이 붙은 컨트롤러 메서드로 라우팅
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // STOMP WebSocket 연결을 위한 엔드포인트 등록
        // 클라이언트는 ws://localhost:8080/ws-chat 로 연결
        // setAllowedOrigins("*")는 개발 시 CORS 문제 방지 (운영 시에는 특정 도메인으로 제한 권장)
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS(); // SockJS는 웹소켓 미지원 브라우저 호환성 제공
    }
}
