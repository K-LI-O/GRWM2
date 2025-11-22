package GRWM.backend.dto.community;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {


    private PostContentDto content;

    private String visibility; // 설정하지 않을 시 public

    private List<String> hashtags;

}
