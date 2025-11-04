package GRWM.backend.entity.community;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PostHashtag {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    Post post;

    @ManyToOne
    Hashtag hashtag;

    @CreatedDate
    @Column(updatable = false)
    LocalDateTime createdAt;

    public PostHashtag(Post post, Hashtag hashtag){
        this.post = post;
        this.hashtag = hashtag;
    }



    public static PostHashtag createPostHashtag(Post post, Hashtag hashtag) {

        // 1. PostHashtag 객체 생성 (일반 생성자를 private으로 만들고 팩토리 메서드 사용)
        PostHashtag postHashtag = new PostHashtag(post, hashtag);

        // Post에 관계 설정 (Post의 addPostHashtag 메서드 호출)
        post.getPostHashtagList().add(postHashtag);

        // Hashtag에 관계 설정 (Hashtag에도 유사한 컬렉션이 있다면 추가해야 함)
        hashtag.getPostHashtagList().add(postHashtag);

        return postHashtag;
    }
}
