package GRWM.backend.dto.studyroom;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomBriefDto {
    String name;
    CommunityUserBriefDto creator;
    String category;
    String description;

}
