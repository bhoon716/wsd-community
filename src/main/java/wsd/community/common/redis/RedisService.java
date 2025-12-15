package wsd.community.common.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public String getValues(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? null : String.valueOf(value);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
