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
            "(:userId IS NULL OR d.member.id = :userId) AND " + // ⭐️ d.member.id로 수정
            "(:date IS NULL OR d.date = :date) AND " +
            "(:category IS NULL OR d.category = :category) AND " +
            "(:emotion IS NULL OR d.emotion = :emotion) AND " +
            // ⭐️ LIKE '%:keyword%' 문법으로 수정
            "(:keyword IS NULL OR d.content LIKE %:keyword% OR d.title LIKE %:keyword%)")
    Page<Diary> findByMember_IdAndDateAndCategoryAndEmotionAndKeyword(Long userId, LocalDate date, String category, Emotion emotion, String keyword, Pageable pageable);

    Page<Diary> findByMember_Id(Long userId);
}
