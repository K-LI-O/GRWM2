package GRWM.backend.dto.studyroom;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyRoomCreateDto {
    /*
    name: string; (스터디룸 이름)
category: string; (카테고리)
String description; (설명)
int duration; (분 단위 지속시간)
int extensionTime; 연장시간 (분 단위)
boolean isPrivate; 공개 여부 (isPublic 이었는데 private으로 다른 파트와 통일)
     */
    private String name;
    private String category;
    private String description;
    private int duration;
    private int extensionTime;
    private boolean isPrivate;
    private String password;

}
