package GRWM.backend.service;

import GRWM.backend.config.FileStorageConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private final Path fileStorageLocation; // 저장 디렉토리 경로;
    private static final String WEB_ACCESS_PREFIX = "/images/";
    private final String domain = "http://localhost:8080"; // 실제 배포 시에는 도메인 주소를 사용

    // [예시] 파일명(DB 저장값)을 받아 완전한 URL로 변환하여 DTO에 설정합니다.
    public String getImageUrl(String savedFileName) {
        // http://localhost:8080/images/a1b2c3d4-....jpg 와 같은 URL을 반환
        return domain + WEB_ACCESS_PREFIX + savedFileName;
    }


    public FileService(FileStorageConfig fileStorageConfig) {
        // 설정에서 읽은 디렉토리를 Path 객체로 초기화
        this.fileStorageLocation = Paths.get(fileStorageConfig.getDir())
                .toAbsolutePath().normalize();

        try {
            // 저장 디렉토리가 없으면 생성
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("파일을 저장할 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    /**
     * 파일을 저장하고, 저장된 고유 파일 이름을 반환합니다.
     */
    public String storeFile(MultipartFile file) {
        // 1. 파일 이름 유효성 검사 및 고유 이름 생성
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 파일 이름 충돌을 피하기 위해 UUID 사용
        String fileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // 2. 파일 저장 경로 설정
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            // 3. 파일 저장 (덮어쓰기 옵션으로)
            Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            return getImageUrl(fileName); // 고유 파일 이름 반환

        } catch (IOException ex) {
            throw new RuntimeException("파일 저장에 실패했습니다.", ex);
        }
    }



}
