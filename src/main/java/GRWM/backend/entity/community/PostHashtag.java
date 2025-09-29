package GRWM.backend.entity.community;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration;

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

    public PostHashtag(Post post, Hashtag hashtag){
        this.post = post;
        this.hashtag = hashtag;

        if (post != null && post.getPostHashtagList() != null) {
            post.getPostHashtagList().add(this);
        }
    }
}
