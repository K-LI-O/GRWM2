package GRWM.backend.dto.tracker;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TomorrowMessageCreateDto {
    String content;
    LocalDateTime scheduledTime;
}
