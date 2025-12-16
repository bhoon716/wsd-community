package wsd.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import wsd.community.redis.RedisService;

@SpringBootTest
class CommunityApplicationTests {

	@MockitoBean
	private RedisService redisService;

	@Test
	void contextLoads() {
	}

}
