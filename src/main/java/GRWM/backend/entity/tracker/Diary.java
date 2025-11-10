package GRWM.backend.entity.tracker;

import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private Member member;
    @CreatedDate
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDate date;

    private String category;
    private String title;
    private String content;
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    private Emotion emotion;


}
