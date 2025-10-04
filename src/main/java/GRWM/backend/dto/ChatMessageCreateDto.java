package GRWM.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageCreateDto {

    private Long chatRoomId;
    private String content;
    private Long communityId; // userId 넘겨주심 됩니도.
}
