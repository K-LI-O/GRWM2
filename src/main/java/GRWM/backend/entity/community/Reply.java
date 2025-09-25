package GRWM.backend.entity.community;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Entity
@RequiredArgsConstructor
public class Reply {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Comment rootComment;

    @OneToOne
    private Comment reply;

}
