package GRWM.backend.entity.community;

import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    private List<String> imageLink = new ArrayList<>();

    private String content;

    private String visibility; //모두 / 친구 / 나만

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean isEdited = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private CommunityUser user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Liked> likedList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<PostHashtag> postHashtagList = new ArrayList<>();


    public int countLikes(){
        return this.likedList.size();
    }

    public int countComments(){
        return this.commentList.size();
    }



    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtagList.add(postHashtag);
        // 양방향 관계 설정: PostHashtag에 Post 객체도 설정
        if (postHashtag.getPost() != this) {
            postHashtag.setPost(this);
        }
    }


    public Post(CommunityUser user, List<String> imageLink, String content, String visibility){
        this.user = user;
        this.imageLink = imageLink;
        this.content = content;
        this.visibility = visibility;
    }

}
