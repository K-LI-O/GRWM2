package GRWM.backend.entity.community;

import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean isEdited;

    private boolean isPrivate;



    @ManyToOne
    private CommunityUser user;

    @ManyToOne
    private Post post;

    // 부모 댓글(Parent Comment)을 참조합니다. 최상위 댓글의 경우 이 값은 null입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();



    public Comment(Post post, CommunityUser user, Comment parentComment, String content, boolean isPrivate) {
        this.post = post;
        this.user = user;
        this.parentComment = parentComment;
        this.content = content;
        this.isPrivate = isPrivate;
    }
}
