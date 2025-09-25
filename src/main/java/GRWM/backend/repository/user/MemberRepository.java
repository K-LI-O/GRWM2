package GRWM.backend.repository.user;

import GRWM.backend.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /*
    함수명 : existsByLogin_id
    기능 : 일치하는 로그인 아이디가 존재하면 True,  존재하지 않으면 False 반환
    매개변수 : String
    반환값 : boolean
     */
    boolean existsByLoginId(String loginId);

    Optional<Member> findByLoginId(String loginId);

    @Query("SELECT m.username FROM Member m WHERE m.loginId = :loginId")
    Optional<String> findUsernameByLoginId(@Param("loginId") String loginId);

    @Query("SELECT m.id FROM Member m WHERE m.loginId = :loginId")
    Optional<Long> findIdByLoginId(@Param("loginId") String loginId);

}
