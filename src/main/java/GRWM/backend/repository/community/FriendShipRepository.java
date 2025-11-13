package GRWM.backend.repository.community;

import GRWM.backend.entity.community.Friendship;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendShipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUser1_IdAndUser2_Id(Long userId1, Long userId2);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Friendship f " +
            "WHERE (f.user1 = :user1 AND f.user2 = :user2) " +
            "OR (f.user1 = :user2 AND f.user2 = :user1)")
    boolean existsFriendshipBetweenUsers(@Param("user1") CommunityUser user1, @Param("user2") CommunityUser user2);

    @Query("SELECT f " + // Friendship 엔티티 자체를 선택
            "FROM Friendship f " +
            "WHERE (f.user1 = :user1 AND f.user2 = :user2) " +
            "OR (f.user1 = :user2 AND f.user2 = :user1)")
    Optional<Friendship> findFriendshipBetweenUsers(@Param("user1") CommunityUser user1, @Param("user2") CommunityUser user2);


}
