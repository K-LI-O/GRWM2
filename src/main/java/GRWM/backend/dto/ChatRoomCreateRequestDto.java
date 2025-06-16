package GRWM.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateRequestDto {

    private Long userId;

    private String roomName;

    private String description;

    private boolean isPrivate; // 공개(false) / 비공개방(true) = 비밀번호 유무와 동일

    private String password;  // 비공개방인 경우 입장 비밀번호 (서버에서만 사용)

    private int maxMembers;


}
