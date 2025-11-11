package GRWM.backend.config;

import GRWM.backend.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;

import GRWM.backend.jwt.JwtAuthenticationFilter;
import GRWM.backend.jwt.JwtTokenProvider;
import GRWM.backend.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // 이 클래스가 스프링 설정 클래스임을 명시
@EnableWebSecurity // 스프링 시큐리티 기능을 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // JWT를 사용하므로 CSRF 보호 비활성화
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/ws/**").permitAll() // Ensure this matches your STOMP endpoint
                        .requestMatchers("/api/chat-room/create").permitAll() // 임시 테스트용
                        .requestMatchers("/api/auth/**").permitAll() // 로그인, 회원가입 경로는 허용
                        .requestMatchers("/oauth2").permitAll() // 로그인, 회원가입 경로는 허용
                        .requestMatchers("/favicon.ico", "/login**", "/login?error**", "/error**").permitAll() // /login, /login?error 등 허용
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated() // 나머지 요청은 JWT 인증 필요

                )
                .exceptionHandling(exception -> exception
                        // 인증되지 않은 사용자가 인증 필요 경로에 접근 시, 로그인 페이지로 리디렉션 대신 401 응답을 반환하도록 설정
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                // 3. OAuth2 로그인 설정 (JWT 발행 로직 연결 필요)
                .oauth2Login(oauth2 -> oauth2
                        // OAuth2 인증 성공 후 토큰 발행 및 응답을 처리할 핸들러 등록
                        .successHandler(customOAuth2SuccessHandler) // SuccessHandler를 구현하여 JWT 발행 로직 구현
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOAuth2UserService)
                        )
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestRepository(cookieAuthorizationRequestRepository()) // 👈 적용
                        )
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
        // 세션 대신 쿠키에 인증 요청 상태를 저장합니다.
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    // PasswordEncoder, UserDetailsService 등 필요한 다른 Bean들도 여기에 정의할 수 있습니다.
    // 예시:
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

}
