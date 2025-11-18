package GRWM.backend.config;

import GRWM.backend.dto.auth.LoginTokenResponse;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.entity.user.Member;
import GRWM.backend.jwt.JwtTokenProvider;
import GRWM.backend.repository.user.CommunityUserRepository;
import GRWM.backend.repository.user.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CommunityUserRepository communityUserRepository;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        try{

        // 2. 인증 객체에서 CustomUserDetails 추출
        // authentication.getPrincipal()은 CustomUserDetails (혹은 CustomUser)를 반환함
        Object principal = authentication.getPrincipal();

        if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) principal;
            // oidcUser에서 필요한 정보를 추출하여 JWT 생성 또는 DB 로직을 수행
            oidcUser.getUserInfo();
            String email = oidcUser.getEmail();
            String imageLink = oidcUser.getPicture();
            String username = oidcUser.getFullName();
            String sub = oidcUser.getAttribute("sub");
            Member member = memberRepository.findByGoogleId(sub).orElseThrow(() ->
                    new NoSuchElementException("Member not found for Google ID: " + sub));

            CommunityUser user = communityUserRepository.findById(member.getId()).orElseThrow(() ->
                    new NoSuchElementException("CommunityUser not found for Member ID: " + member.getId()));

            // 만약 CustomUserDetails가 필요하다면 새로운 CustomUserDetails를 생성
            CustomUserDetails userDetails = new CustomUserDetails(member, user.getNickname(), oidcUser.getAttributes());
//          // 3. JWT 토큰 생성
            String accessToken = jwtTokenProvider.generateTokenWithCUD(userDetails, authentication);

            // 4. 응답 본문(Body)에 담을 객체 생성 (예: TokenResponseDto)
            //TokenResponseDto tokenResponse = new TokenResponseDto(accessToken, "Bearer");
            LoginTokenResponse loginTokenResponse = new LoginTokenResponse(accessToken, "Bearer", userDetails.getUsername(), userDetails.getUserId(),
                    communityUserRepository.findById(userDetails.getCommunityUserId()).orElseThrow().getNickname());


            // 5. JSON 응답 전송

            String encodedNickname = URLEncoder.encode(userDetails.getCommunityUserNickname(), StandardCharsets.UTF_8);
            String fragment = "#token=" + accessToken +
                                "&userId=" + userDetails.getUserId() +
                                "&username=" + userDetails.getUsername() +
                                "&communityNickname=" + encodedNickname;
            String redirectUrl = "http://localhost:3000/main" + fragment; // 예시ㅓ
            response.sendRedirect(redirectUrl);

//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.setStatus(HttpServletResponse.SC_OK);

            // Java 객체(LoginTokenResponse)를 JSON 문자열로 변환 (Jackson ObjectMapper 사용 예시)
            // ObjectMapper는 보통 Spring Boot의 환경설정으로 빈(Bean)으로 등록되어 있어, 주입받아 사용하거나 직접 생성 가능
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonResponse = objectMapper.writeValueAsString(loginTokenResponse);
//
//            response.getWriter().write(jsonResponse);
        }
        } catch (Exception e) {
            // 🚨 예외 상세 정보를 콘솔에 출력
            System.err.println("SUCCESS HANDLER RUNTIME EXCEPTION: " + e.getMessage());
            e.printStackTrace();

            // 예외가 발생했으니, 명시적으로 실패 페이지로 리다이렉션하여 Spring Security의 기본 흐름을 방해하지 않습니다.
            response.sendRedirect("/login?error");
        }
    }
}