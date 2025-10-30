package GRWM.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.upload")
@Getter
@Setter

public class FileStorageConfig {
    private String dir; // ${user.home}/project-files/profile-images



}
