package GRWM.backend.dto.community;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDto {

    private String nickname;
    private String profileImage;
    private String description;
    private String bannerImage;
}
