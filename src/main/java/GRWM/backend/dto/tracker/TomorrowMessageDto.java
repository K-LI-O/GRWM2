package GRWM.backend.dto.tracker;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TomorrowMessageDto {
    Long messageId;
    LocalDateTime scheduledTime;
    String content;
}
