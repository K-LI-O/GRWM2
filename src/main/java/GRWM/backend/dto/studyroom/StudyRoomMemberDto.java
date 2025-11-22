package GRWM.backend.dto.studyroom;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyRoomMemberDto {
    String type;
    CommunityUserBriefDto user;
}
