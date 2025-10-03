package GRWM.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomJoinDto {

    private Long userId;

    private String chatName;

    private Boolean isManager; // 새로운 어쩌구.
}
