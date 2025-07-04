package GRWM.backend.repository;

import GRWM.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
