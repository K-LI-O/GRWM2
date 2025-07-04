package GRWM.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // 이 클래스가 스프링 설정 클래스임을 명시
@EnableWebSecurity // 스프링 시큐리티 기능을 활성화
public class SecurityConfig {

    @Bean // SecurityFilterChain 빈 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/public/**").permitAll() // 공개 API
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .formLogin(withDefaults()); // 기본 폼 로그인 활성화 (또는 다른 인증 방식)
        // .httpBasic(withDefaults()); // HTTP Basic 인증
        // .csrf(csrf -> csrf.disable()); // REST API 서버의 경우 CSRF 비활성화 고려

        return http.build(); // HttpSecurity 설정을 기반으로 SecurityFilterChain 생성
    }

    // PasswordEncoder, UserDetailsService 등 필요한 다른 Bean들도 여기에 정의할 수 있습니다.
    // 예시:
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

}
