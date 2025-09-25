package GRWM.backend.entity.community;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor
public class PostHashtag {

    @Id
    @GeneratedValue
    private Long id;
}
