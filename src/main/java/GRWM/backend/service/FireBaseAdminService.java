package GRWM.backend.service;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FireBaseAdminService {

    // application.properties 에서 설정한 경로 값을 주입
    @Value("${firebase.service-account.path:}")
    private String serviceAccountPath;

    @PostConstruct // 애플리케이션 시작 시 자동 실행
    public void initialize() {

        if (serviceAccountPath.isEmpty()) {
            System.err.println("Firebase Admin SDK 초기화 실패: 'firebase.service-account.path' 속성이 설정되지 않았습니다.");
            return; // 초기화 중단
        }

        try {
            // 1. 서비스 계정 키 파일 로드
            FileInputStream serviceAccount =
                    new FileInputStream(serviceAccountPath); // 실제 경로로 변경 필요

            // 2. Firebase 옵션 설정
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // 3. Admin SDK 초기화
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            // 초기화가 완료되면, SDK 내부적으로 OAuth 2.0 Access Token 관리

        } catch (IOException e) {
            // 키 파일 로드 실패 등 예외 처리
            System.err.println("Firebase Admin SDK 초기화 실패: " + e.getMessage());
        }
    }
}
