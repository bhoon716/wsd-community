package wsd.community.common.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bandwidth limit() {
        // IP당 1분에 100개의 요청을 허용
        return Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
    }
}
