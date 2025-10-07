package GRWM.backend.controller;

import GRWM.backend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class FileController {

    private final FileService fileService;
    // URL 매핑 경로 (Controller에 설정된 @RequestMapping과 동일)
    private static final String FILE_ACCESS_BASE_PATH = "/api/files/";


    @PostMapping("/upload-image")
    public String uploadProfileImage(@RequestParam("file") MultipartFile file) {
        // 1. 파일 저장 서비스 호출 (저장 후 고유 파일 이름 반환)
        String fileName = fileService.storeFile(file);

        // 2. 파일에 접근 가능한 URL 생성
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath() // http://localhost:8080
                .path(FILE_ACCESS_BASE_PATH)                                        // /api/files/
                .path(fileName)                                                     // [고유 파일 이름]
                .toUriString(); // 최종 URL 생성 (예: http://localhost:8080/api/files/abc-123.jpg)

        // 3. (추가 로직): 생성된 URL을 사용자 엔티티의 profileImageUrl 필드에 저장


        return fileDownloadUri; // 클라이언트에게 URL 반환
    }
}
