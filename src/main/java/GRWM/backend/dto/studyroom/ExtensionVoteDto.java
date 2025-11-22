package GRWM.backend.dto.studyroom;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExtensionVoteDto {
    int agreeCount; // (찬성 투표자 수)
    int votedCount; // (투표자 수)
    int totalParticipants; // (전체 참여자 수)
    boolean isCompleted; // (투표가 완료되었는지의 여부)
    boolean result; // (결과; 투표가 완료되기 전에는 사용하지 말 것)
    String type;
}


