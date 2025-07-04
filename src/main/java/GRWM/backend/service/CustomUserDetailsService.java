package GRWM.backend.service;

import GRWM.backend.entity.CustomUserDetails;
import GRWM.backend.entity.Member;
import GRWM.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    /**
     * 사용자 이름(로그인 ID)을 기반으로 UserDetails 객체를 로드합니다.
     * 스프링 시큐리티의 인증 과정에서 자동으로 호출됩니다.
     *
     * //@param username 사용자가 입력한 로그인 ID
     * //@return UserDetails 구현체 (MemberDetails)
     * //@throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public CustomUserDetails loadUserByUsername(String loginId){
        // 1. 데이터베이스에서 해당 username(여기서는 loginId)을 가진 Member를 조회합니다.
        // Optional을 사용하여 null 처리
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId));

        // 2. 조회된 Member 객체를 기반으로 UserDetails 구현체 (MemberDetails)를 생성하여 반환합니다.
        // MemberDetails는 Member 객체의 정보를 스프링 시큐리티가 요구하는 UserDetails 형태로 변환합니다.
        return new CustomUserDetails(member);
    }
}
