package GRWM.backend.repository.community;

import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.community.PostHashtag;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Slice<Post> findAllByUserIn(List<CommunityUser> followingList, Pageable pageable);



    Slice<Post> findByUser(CommunityUser user, Pageable pageable);

    Slice<Post> findByContentContainingAndVisibility(String keyword, String visibility, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.user.id IN :followingUserIds " + // 1. 팔로우 대상의 게시물만 필터링
            "AND (" +
            "    p.visibility = 'public' " + // 2. Public 게시물은 무조건 포함
            "    OR (" +
            "        p.user.id = :readerId " + // 3. 조회자(:readerId)가 곧 작성자(:userId) 본인인 경우
            "    )" +
            "    OR (" +
            "        p.visibility = 'friends' AND " + // 3. Friends 게시물은 조건을 만족할 때 포함
            "        EXISTS (" + // 4. Friendship 테이블을 사용하여 맞팔로우 관계(친구) 확인
            "            SELECT f FROM Friendship f " +
            "            WHERE (f.user1.id = p.user.id AND f.user2.id = :readerId) " +
            "            OR (f.user1.id = :readerId AND f.user2.id = p.user.id)" +
            "        )" +
            "    )" +
            ") " +
            "ORDER BY p.createdAt DESC")
    Slice<Post> findTimelinePostsWithVisibility(
            @Param("readerId") Long readerId,
            @Param("followingUserIds") List<Long> followingUserIds,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p " +
            "WHERE p.user.id = :userId " + // 1. userId가 포스트 작성자와 같은 것;
            "AND (" +
            "    p.visibility = 'public' " + // 2. Public 게시물은 무조건 포함
            "    OR (" +
            "        p.user.id = :readerId " + // 3. 조회자(:readerId)가 곧 작성자(:userId) 본인인 경우
            "    )" +
            "    OR (" +
            "        p.visibility = 'friends' AND " + // 3. Friends 게시물은 조건을 만족할 때 포함
            "        EXISTS (" + // 4. Friendship 테이블을 사용하여 맞팔로우 관계(친구) 확인
            "            SELECT f FROM Friendship f " +
            "            WHERE (f.user1.id = p.user.id AND f.user2.id = :readerId) " +
            "            OR (f.user1.id = :readerId AND f.user2.id = p.user.id)" +
            "        )" +
            "    )" +
            ") " +
            "ORDER BY p.createdAt DESC")
    Slice<Post> findUserPagePostsWithVisibility(
            @Param("readerId") Long readerId,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p " +
            "WHERE p.content LIKE CONCAT('%', :keyword, '%') "+ // 1. 키워드가 내용에 포함된 것
            "AND (" +
            "    p.visibility = 'public' " + // 2. Public 게시물은 무조건 포함
            "    OR (" +
            "        p.user.id = :readerId " + // 3. 조회자(:readerId)가 곧 작성자(:userId) 본인인 경우
            "    )" +
            "    OR (" +
            "        p.visibility = 'friends' AND " + // 3. Friends 게시물은 조건을 만족할 때 포함
            "        EXISTS (" + // 4. Friendship 테이블을 사용하여 맞팔로우 관계(친구) 확인
            "            SELECT f FROM Friendship f " +
            "            WHERE (f.user1.id = p.user.id AND f.user2.id = :readerId) " +
            "            OR (f.user1.id = :readerId AND f.user2.id = p.user.id)" +
            "        )" +
            "    )" +
            ") " +
            "ORDER BY p.createdAt DESC")
    Slice<Post> findSearchPostsWithVisibility(
            @Param("keyword") String keyword,
            @Param("readerId") Long readerId,
            Pageable pageable
    );

    @Query("SELECT p.post FROM PostHashtag p " +
            "WHERE p.hashtag.id IN :hashtagIds "+ // 1. 키워드가 내용에 포함된 것
            "AND (" +
            "    p.post.visibility = 'public' " + // 2. Public 게시물은 무조건 포함
            "    OR (" +
            "        p.post.user.id = :readerId " + // 3. 조회자(:readerId)가 곧 작성자(:userId) 본인인 경우
            "    )" +
            "    OR (" +
            "        p.post.visibility = 'friends' AND " + // 3. Friends 게시물은 조건을 만족할 때 포함
            "        EXISTS (" + // 4. Friendship 테이블을 사용하여 맞팔로우 관계(친구) 확인
            "            SELECT f FROM Friendship f " +
            "            WHERE (f.user1.id = p.post.user.id AND f.user2.id = :readerId) " +
            "            OR (f.user1.id = :readerId AND f.user2.id = p.post.user.id)" +
            "        )" +
            "    )" +
            ") " +
            "ORDER BY p.post.createdAt DESC")
    Slice<Post> findPostsByHashtagAndVisibility(@Param("hashtagIds") List<Long> hashtagIds, @Param("readerId") Long readerId, Pageable pageable);


    @Query("SELECT p.post FROM PostHashtag p " +
            "WHERE p.hashtag.id = :hashtagId "+ // 1. 키워드가 내용에 포함된 것
            "AND (" +
            "    p.post.visibility = 'public' " + // 2. Public 게시물은 무조건 포함
            "    OR (" +
            "        p.post.user.id = :readerId " + // 3. 조회자(:readerId)가 곧 작성자(:userId) 본인인 경우
            "    )" +
            "    OR (" +
            "        p.post.visibility = 'friends' AND " + // 3. Friends 게시물은 조건을 만족할 때 포함
            "        EXISTS (" + // 4. Friendship 테이블을 사용하여 맞팔로우 관계(친구) 확인
            "            SELECT f FROM Friendship f " +
            "            WHERE (f.user1.id = p.post.user.id AND f.user2.id = :readerId) " +
            "            OR (f.user1.id = :readerId AND f.user2.id = p.post.user.id)" +
            "        )" +
            "    )" +
            ") " +
            "ORDER BY p.post.createdAt DESC")
    Slice<Post> searchPostsByHashtagAndVisibility(@Param("hashtagId") Long hashtagId, @Param("readerId") Long readerId, Pageable pageable);



}
