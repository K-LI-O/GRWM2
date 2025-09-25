package GRWM.backend.entity.community;

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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    private String title;

    private List<String> imageLink;

    private String content;

    private String visibility;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    private CommunityUser user;

    @OneToMany(mappedBy = "post")
    private List<Like> likeList;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;



}
