package GRWM.backend.dto.tracker;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryDto {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String category;
    String title;
    String content;
    private List<String> tags;
    String emotion;

}
