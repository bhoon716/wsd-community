package wsd.community.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String value, Duration duration) {
        log.debug("Redis 값 저장: key={}", key);
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public String getValues(String key) {
        log.debug("Redis 값 조회: key={}", key);
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? null : String.valueOf(value);
    }

    public void deleteValues(String key) {
        log.debug("Redis 값 삭제: key={}", key);
        redisTemplate.delete(key);
    }
}
