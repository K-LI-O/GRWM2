package GRWM.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileStorageConfig fileStorageConfig;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // [1] 핸들러 URL: 클라이언트가 요청할 URL 접두사
        String handler = "/images/**";

        // [2] 실제 경로: file: 접두사를 붙여 파일 시스템 경로임을 명시
        // fileStorageConfig.getDir() = "${user.home}/project-files/profile-images" 가정
        String locations = "file:" + fileStorageConfig.getDir();

        registry.addResourceHandler(handler)
                .addResourceLocations(locations);

        // ⚠️ 최종 적용 예시: http://localhost:8080/images/a1b2c3d4.jpg 요청 시
        //                 서버는 -> /home/user/project-files/profile-images/a1b2c3d4.jpg 파일을 찾아 반환합니다.
    }
}
