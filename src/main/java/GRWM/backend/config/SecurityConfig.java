package GRWM.backend.config;

import GRWM.backend.handler.CustomAccessDeniedHandler;
import GRWM.backend.jwt.JwtAuthenticationFilter;
import GRWM.backend.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // 이 클래스가 스프링 설정 클래스임을 명시
@EnableWebSecurity // 스프링 시큐리티 기능을 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .csrf(csrf -> csrf.disable()) // JWT를 사용하므로 CSRF 보호 비활성화
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/ws/chatroom/**").permitAll() // Ensure this matches your STOMP endpoint
                        .requestMatchers("/api/chat-room/create").permitAll() // 임시 테스트용
                        .requestMatchers("/api/auth/**").permitAll() // 로그인, 회원가입 경로는 허용
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated() // 나머지 요청은 JWT 인증 필요

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


    // PasswordEncoder, UserDetailsService 등 필요한 다른 Bean들도 여기에 정의할 수 있습니다.
    // 예시:
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

}
