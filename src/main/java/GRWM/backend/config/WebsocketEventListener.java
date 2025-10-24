package GRWM.backend.config;

import GRWM.backend.dto.chatroom.ChatMessageCreateDto;
import GRWM.backend.dto.chatroom.ChatMessageDto;
import GRWM.backend.entity.chatroom.ChatMessage;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.repository.user.CommunityUserRepository;
import GRWM.backend.service.chatroom.ChatMessageService;
import GRWM.backend.service.community.CommunityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Component // ⬅️ Spring 빈으로 등록하여 이벤트 리스너로 작동하게 합니다.
@RequiredArgsConstructor
public class WebsocketEventListener {

    private final SimpMessagingTemplate messagingTemplate; // 메시지 전파 도구
    private final ChatMessageService chatService;
    private final CommunityUserRepository communityUserRepository;

    /**
     * 클라이언트가 채팅방 토픽을 구독(SUBSCRIBE)했을 때 (입장 시점) 처리
     */
    @EventListener
    public void handleSessionSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser(); // ⬅️ Authentication 객체 (Principal 타입)

        if (principal == null) {
            // 알 수 없습니다: 인증되지 않은 사용자 (추측입니다: 웹소켓 연결 전에 인증이 필요합니다.)
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        // 1. 사용자 ID 추출 (UserDetails 구현체에서)
        Authentication auth = (Authentication) principal;
        // UserDetails 구현체 (당신의 사용자 정보 클래스)로 캐스팅
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long communityId = userDetails.getCommunityUserId(); // ⬅️ 정의한 UserDetails 구현체에서 ID 필드 접근

        String destination = accessor.getDestination(); // 구독 주소: /topic/chat.{roomId}

        if (destination != null && destination.startsWith("/topic/chat.")) {
        Long chatRoomId = extractChatRoomId(destination);

        if (chatRoomId != null) {
            // 2. 서비스 호출: 메시지 생성 및 DB 저장
            ChatMessageCreateDto dto = new ChatMessageCreateDto(
                    chatRoomId,
                    communityUserRepository.findById(
                            communityId).get().getNickname()+"님이 입장하였습니다.",
                    null,
                    communityId);

            ChatMessageDto joinMessage = chatService.saveMessage(chatRoomId, dto, communityId, ChatMessage.MessageType.JOIN);

            // 3. 메시지 브로커를 통해 전파
            messagingTemplate.convertAndSend(destination, joinMessage);
            }
        }
    }



        // 주소에서 채팅방 ID를 추출하는 헬퍼 메서드 (예시)
        private Long extractChatRoomId(String destination) {
            // 예: "/topic/chat.123" -> "123" 추출

            if (destination == null || !destination.contains(".")) {
                // 확실하지 않음: destination이 null이거나 '.'을 포함하지 않으면 유효하지 않음
                return null;
            }
            int lastDotIndex = destination.lastIndexOf('.');
            String chatRoomIdString = destination.substring(lastDotIndex + 1);

            try {
                // 3. 추출된 문자열을 Long 타입으로 변환합니다.
                return Long.valueOf(chatRoomIdString);
            } catch (NumberFormatException e) {
                // 확실하지 않음: 추출된 부분이 숫자가 아닐 경우 (예: "abc")
                System.err.println("Chat Room ID 형식이 잘못되었습니다: " + chatRoomIdString);
                return null;
            }
        }





}
