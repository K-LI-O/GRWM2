package GRWM.backend.controller.tracker;

import GRWM.backend.dto.tracker.TomorrowMessageCreateDto;
import GRWM.backend.dto.tracker.TomorrowMessageDto;
import GRWM.backend.service.tracker.TomorrowMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TomorrowMessageController {

    private final TomorrowMessageService service;

    /*
    name : getTomorrowMessage
    function : 미래 메시지 조회
    URL: GET /api/users/{userId}/future-message/{messageId}
    param: Long userId, Long messagedId
    Response: FutureMessageDto;
    */
    @GetMapping("/api/users/{userId}/future-message")
    public TomorrowMessageDto getTomorrowMessage(@PathVariable Long userId){
        return service.getTomorrowMessage(userId);
    }

    /*
    name : createTomorrowMessage
    function : 미래 메시지 생성
    URL : POST /api/users/{userId}/future-message
   param : Long userId,
Request Body:
{
content: string; // 메시지 내용
LocalDateTime scheduledTime;
}
Response: FutureMessageDto;
    */
    @PostMapping("/api/users/{userId}/future-message")
    public TomorrowMessageDto createTomorrowMessage(@PathVariable Long userId,
                                                    @RequestBody TomorrowMessageCreateDto dto) throws Exception {
        return service.createTomorrowMessage(userId, dto);
    }

    /*
    name : updateTomorrowMessage
    function : 미래 메시지 수정
    URL: PUT /api/users/{userId}/future-message
    param : Long userId, FutureMessageDto
    Response: FutureMessageDto;
    */
    @PutMapping("/api/users/{userId}/future-message")
    public TomorrowMessageDto updateTomorrowMessage(@PathVariable Long userId,
                                                    @RequestBody TomorrowMessageDto dto) throws Exception{
        return service.updateTomorrowMessage(userId, dto);
    }

    /*
    name : deleteTomorrowMessage
    function : 미래 메시지 삭제
    URL: DELETE /api/users/{userId}/future-message/{messageId}
    param : Long userId, Long messageId
    return value : -
    */
    @DeleteMapping("/api/users/{userId}/future-message/{messageId}")
    public ResponseEntity<Void> deleteTomorrowMessage(@PathVariable Long userId,
                                                      @PathVariable Long messageId){
        service.deleteTomorrowMessage(userId, messageId);
        return ResponseEntity.noContent().build();
    }

}
