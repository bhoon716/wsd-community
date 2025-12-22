package wsd.community.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("!test")
public class FirebaseConfig {

    @Value("${app.firebase.config}")
    private String firebaseConfig;

    @Bean
    public GoogleCredentials googleCredentials() {
        try {
            InputStream serviceAccount;
            String trimmedConfig = firebaseConfig.trim();

            if (trimmedConfig.startsWith("{")) {
                log.info("Firebase 설정: JSON 내용에서 로드 중");
                serviceAccount = new ByteArrayInputStream(trimmedConfig.getBytes(StandardCharsets.UTF_8));
            } else {
                log.info("Firebase 설정: 파일 경로에서 로드 중: {}", trimmedConfig);
                serviceAccount = new FileInputStream(trimmedConfig);
            }

            return GoogleCredentials.fromStream(serviceAccount);
        } catch (IOException e) {
            log.error("Google Credentials 로드 실패: {}", e.getMessage());
            throw new RuntimeException("Google Credentials 로드 실패", e);
        }
    }

    @Bean
    public FirebaseApp firebaseApp(GoogleCredentials credentials) {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("FirebaseApp 초기화 성공");
            return app;
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
