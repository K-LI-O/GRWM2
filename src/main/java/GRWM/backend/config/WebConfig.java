package GRWM.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 1. CORS를 적용할 URL 패턴 (예: "/api"로 시작하는 모든 요청)
                .allowedOrigins("http://localhost:3000") // 2. 허용할 출처(Origin) 목록
                //  - 여러 개 지정 가능
                //  - "*"은 모든 출처 허용 (보안상 권장되지 않음, 개발 시에만 사용)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 3. 허용할 HTTP 메서드
                .allowedHeaders("*") // 4. 허용할 요청 헤더 (예: Content-Type, Authorization 등, "*"은 모든 헤더)
                .allowCredentials(true) // 5. 클라이언트가 자격 증명(쿠키, HTTP 인증)을 포함한 요청을 보낼지 여부
                .maxAge(3600); // 6. Pre-flight 요청 결과를 캐싱할 시간 (초 단위)
        //    이 시간 동안 브라우저는 동일한 Pre-flight 요청을 다시 보내지 않습니다.
    }


}
