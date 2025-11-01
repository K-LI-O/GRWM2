package GRWM.backend.entity.tracker;

import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    private Member member;
    @CreatedDate
    private LocalDateTime createAt;

    private String category;
    private String title;
    private String content;
    private List<String> tags;
    private Emotion emotion;


}
