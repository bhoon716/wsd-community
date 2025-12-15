package wsd.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class CommunityApplicationTests {

	@MockitoBean
	private wsd.community.common.redis.RedisService redisService;

	@Test
	void contextLoads() {
	}

}
