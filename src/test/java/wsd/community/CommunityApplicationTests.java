package wsd.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import wsd.community.redis.RedisService;
import org.springframework.test.context.ActiveProfiles;
import com.google.firebase.auth.FirebaseAuth;

@SpringBootTest
@ActiveProfiles("test")
class CommunityApplicationTests {

	@MockitoBean
	private RedisService redisService;

	@MockitoBean
	private FirebaseAuth firebaseAuth;

	@Test
	void contextLoads() {
	}

}
