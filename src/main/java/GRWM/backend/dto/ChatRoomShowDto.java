package GRWM.backend.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomShowDto {

    private String chatRoomName;

    private String description;

    private boolean isPrivate; // 공개(false) / 비공개방(true) = 비밀번호 유무와 동일

    private int maxMembers;    // 최대 참여 가능 인원 (0 = 무제한)

    private int currentMembers;
}
