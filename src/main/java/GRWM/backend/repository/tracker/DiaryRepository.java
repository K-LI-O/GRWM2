package GRWM.backend.repository.tracker;

import GRWM.backend.entity.tracker.Diary;
import GRWM.backend.entity.tracker.Emotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query(value = "SELECT d FROM Diary d WHERE " +
            "(:date IS NULL OR d.date = :date) AND " +
            "(:category IS NULL OR d.category = :category) AND " +
            "(:emotion IS NULL OR d.emotion = :emotion) AND " +
            // keyword를 title 또는 content에서 검색하도록 JPQL 직접 작성
            "(:keyword IS NULL OR d.content LIKE %:keyword% OR d.title LIKE %:keyword%)")
    Page<Diary> findByDateAndCategoryAndEmotionAndKeyword(LocalDate date, String category, Emotion emotion, String keyword, Pageable pageable);
}
