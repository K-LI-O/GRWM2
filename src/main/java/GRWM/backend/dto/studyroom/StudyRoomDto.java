package GRWM.backend.dto.studyroom;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomDto {

    private String name;
    private CommunityUserBriefDto creator;
    private String category;
    private String description;
    private List<CommunityUserBriefDto> users;
    private int duration; // 지속 시간(분 단위)
    private int extensionTime;// (연장시 몇 분 연장되는지)
    private List<StudyRoomTodoDto> todoList; //(스터디룸에서 작성하는 투두리스트)
    private int extensionCount;
    @Builder.Default
    private int maxMembers = 8;
    private int currentMembers;
    private boolean isPrivate;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
