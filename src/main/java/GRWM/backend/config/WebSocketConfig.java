package GRWM.backend.config;

import GRWM.backend.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.HashMap;


@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

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
        // 클라이언트는 ws://localhost:8080/ws/chatroom 로 연결
        // setAllowedOrigins("*")는 개발 시 CORS 문제 방지 (운영 시에는 특정 도메인으로 제한 권장)
        registry.addEndpoint("/ws/chatroom").setAllowedOriginPatterns("*").withSockJS(); // SockJS는 웹소켓 미지원 브라우저 호환성 제공
        registry.addEndpoint("/ws/chatroom").setAllowedOriginPatterns("*"); // SockJS는 웹소켓 미지원 브라우저 호환성 제공
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() { // <--- This part
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Get the token from the "Authorization" header sent by the client
                    // Client-side: stompClient.connect({ 'Authorization': 'Bearer ' + token }, ...)
                    String authToken = accessor.getFirstNativeHeader("Authorization");

                    if (authToken != null && authToken.startsWith("Bearer ")) {
                        String jwt = authToken.substring(7); // Remove "Bearer " prefix

                        try {
                            if (jwtTokenProvider.validateToken(jwt)) { // Validate your JWT
                                // Get Authentication object from the token
                                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

                                // Set the authenticated user in the SecurityContextHolder
                                // This is crucial for Spring Security to recognize the user
                                SecurityContextHolder.getContext().setAuthentication(authentication);

                                // Set the user on the accessor. This makes the Principal available
                                // for @PreAuthorize or @MessageMapping arguments.
                                accessor.setUser(authentication);
                                System.out.println("WebSocket CONNECT: User authenticated: " + authentication.getName()); // For debugging
                            }
                        } catch (Exception e) {
                            // Log the error, and potentially throw an exception to deny connection
                            System.err.println("WebSocket Authentication failed: " + e.getMessage());
                            accessor.setSessionAttributes(new HashMap<>()); // Clear any attributes
                            accessor.setUser(null); // Explicitly clear user
                            // You might want to throw an exception here to deny the connect frame
                            // throw new MessageDeliveryException("Unauthorized: " + e.getMessage());
                        }
                    } else {
                        System.out.println("WebSocket CONNECT: No Authorization header or malformed.");
                    }
                }
                return message;
            }
        });
    }


}
