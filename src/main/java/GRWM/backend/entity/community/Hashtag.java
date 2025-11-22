package GRWM.backend.entity.community;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor
public class Hashtag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "hashtag")
    private List<CommunityUserHashtag> cuHashtag;

    @OneToMany(mappedBy = "hashtag")
    private List<PostHashtag> postHashtagList;


    public Hashtag(String name){
        this.name = name;
    }


    public void addPostHashtag(PostHashtag postHashtag) {
        // 1. 현재 Hashtag 객체의 컬렉션에 추가
        this.postHashtagList.add(postHashtag);

        // 2. ⭐ 양방향 관계 설정: PostHashtag 엔티티에 현재 Hashtag 객체를 설정
        // 이 로직은 PostHashtag 엔티티 내부에서 setHashtag(this); 역할을 수행해야 합니다.
        // 현재 PostHashtag 엔티티의 구조상setter가 없다면, PostHashtag 내부에 protected setter나
        // PostHashtag.createPostHashtag 메서드를 통해 Hashtag 필드를 설정하는 로직이 필요합니다.

        // (가정: PostHashtag에 protected setHashtag(Hashtag hashtag)가 있다고 가정)
        // postHashtag.setHashtag(this);

        // 그러나 PostHashtag가 이 관계의 주인이므로,
        // PostHashtag 생성 시점에 이미 Hashtag가 설정되어 있다면 아래와 같은 중복 방지 로직이 필요합니다.
        if (postHashtag.getHashtag() != this) {
            // 이 로직은 PostHashtag의 생성(팩토리) 메서드에서 이미 처리되었을 가능성이 높습니다.
            // 여기서는 컬렉션에 추가하는 역할에 집중하는 것이 좋습니다.
        }
    }

}
