package GRWM.backend.service;

import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.user.CommunityUserRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends OidcUserService{

    private final MemberRepository memberRepository; // DB 연동을 위한 Repository 주입
    private final CommunityUserRepository cuRepository;


    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 기본 OAuth2 서비스 호출하여 사용자 정보(OAuth2User) 가져오기
        System.out.println("\n\n\n\n\nloadUser 진입\n\n\n\n\n");
        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();

        // 필수 식별 정보 추출
        String googleId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        System.out.println("\n\n\n\n\n식별 정보 추출\n\n\n\n\n");

        // 2. 받은 정보를 기반으로 DB에 저장(회원가입)하거나 업데이트(로그인)하는 로직 실행
        Optional<Member> memberOptional = memberRepository.findByGoogleId(googleId);
        // 멤버 검색 결과가 존재한다면 업데이트;
        Member savedMember = null;
        if(memberOptional.isPresent()) {
            savedMember = memberOptional.get();
            System.out.println("\n\n\n\n\n멤버 객체 조회" + savedMember.getId() + "\n\n\n\n\n");

        } else { // 회원가입
            Member member = new Member(name, googleId, email);
            savedMember = memberRepository.save(member);

            createCommunityUser(savedMember);
            System.out.println("\n\n\n\n\n멤버 객체 생성" + savedMember.getGoogleId() + "\n\n\n\n\n");
        }

        System.out.println("\n\n\n\n\n결과 반환" + savedMember.getGoogleId() + "\n\n\n\n\n");
        // 3. Spring Security가 사용할 사용자 객체 반환
        return oidcUser;
    }

    @Transactional
    public void createCommunityUser(Member member){
        CommunityUser cu = new CommunityUser();
        cu.setNickname("무명" + member.getId());

        member.setCommunityUser(cuRepository.save(cu));
        memberRepository.saveAndFlush(member);
    }
}
