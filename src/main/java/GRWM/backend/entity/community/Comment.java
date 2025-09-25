package GRWM.backend.entity.community;

import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private boolean isEdited;

    private boolean isPrivate;

    private boolean isReply;



    @ManyToOne
    private CommunityUser user;

    @ManyToOne
    private Post post;

    @OneToMany(mappedBy = "rootComment")
    private List<Reply> rootCommentList;

    @OneToMany(mappedBy = "reply")
    private List<Reply> replyList;


}
