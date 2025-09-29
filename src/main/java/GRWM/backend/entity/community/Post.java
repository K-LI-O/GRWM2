package GRWM.backend.entity.community;

import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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

    private String title;

    private List<String> imageLink;

    private String content;

    private String visibility;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean isEdited = false;

    @ManyToOne
    @JoinColumn
    private CommunityUser user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Liked> likedList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<PostHashtag> postHashtagList;


    public int countLikes(){
        return this.likedList.size();
    }

    public int countComments(){
        return this.commentList.size();
    }



    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtagList.add(postHashtag);
        postHashtag.setPost(this); // 필요에 따라 PostHashtag 에서도 관계 설정
    }


    public Post(CommunityUser user, String title, List<String> imageLink, String content, String visibility){
        this.user = user;
        this.title = title;
        this.imageLink = imageLink;
        this.content = content;
        this.visibility = visibility;
    }

}
