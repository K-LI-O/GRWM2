package GRWM.backend.dto.studyroom;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomBriefDto {
    Long id;
    String name;
    CommunityUserBriefDto creator;
    String category;
    String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
