package GRWM.backend.controller;

import GRWM.backend.dto.NotificationDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    // 알림 목록 불러오기 함수
    /*
    name : getNotifications
    function : 알림 목록을 불러온다(최근 7일간의). 그리고 알림 목록 전송 이후 모두 isRead = true로 바꾸기.
    url : api/users/notifications
    param : userId
    return value : List<NotificationDto>
     */
    @GetMapping
    public List<NotificationDto> getNotifications(@AuthenticationPrincipal CustomUserDetails userDetails){
        return notificationService.getNotifications(userDetails.getUserId());
    }
}
