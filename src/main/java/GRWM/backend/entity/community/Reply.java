package GRWM.backend.entity.community;

import jakarta.persistence.*;
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
    @JoinColumn
    private Comment rootComment;

    @ManyToOne
    @JoinColumn
    private Comment reply;


    public Reply(Comment rootComment, Comment reply){
        this.rootComment = rootComment;
        this.reply = reply;
    }

}
