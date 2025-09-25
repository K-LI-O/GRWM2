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

}
