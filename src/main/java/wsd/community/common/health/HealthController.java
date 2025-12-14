package wsd.community.common.health;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "System", description = "시스템 관련 API")
public class HealthController {

    private final BuildProperties buildProperties;

    @GetMapping("/health")
    @Operation(summary = "헬스 체크", description = "서버 상태를 확인합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "헬스 체크 성공 예시", value = """
            {
                "status": "UP",
                "version": "1.0.0",
                "buildTime": "2025-12-12T10:00:00Z",
                "timestamp": "2025-12-12T10:00:00.123"
            }
            """)))
    public Map<String, Object> checkHealth() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "UP");
        health.put("version", buildProperties.getVersion());
        health.put("buildTime", buildProperties.getTime());
        health.put("timestamp", LocalDateTime.now());
        return health;
    }
}
