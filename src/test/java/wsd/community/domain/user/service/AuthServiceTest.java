package wsd.community.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static wsd.community.redis.RedisConstant.REDIS_RT_PREFIX;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.domain.user.request.ReissueRequest;
import wsd.community.domain.user.response.LoginResponse;
import wsd.community.redis.RedisService;
import wsd.community.security.jwt.JwtTokenProvider;
import com.google.firebase.auth.FirebaseAuth;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private RedisService redisService;

    @MockitoBean
    private FirebaseAuth firebaseAuth;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_success() {
        // given
        User user = userRepository.save(User.builder()
                .email("reissue@test.com")
                .name("tester")
                .role(wsd.community.domain.user.entity.UserRole.USER)
                .build());
        String refreshToken = jwtTokenProvider.generateRefresh(user.getId());

        // Redis에 Refresh Token이 저장되어 있다고 가정
        given(redisService.getValues(REDIS_RT_PREFIX + "reissue@test.com")).willReturn(refreshToken);

        // when
        LoginResponse response = authService.reissue(new ReissueRequest(refreshToken));

        // then
        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
        // Redis 갱신 검증
        verify(redisService).setValues(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() {
        // given
        String accessToken = jwtTokenProvider.generateAccess(1L, "logout@test.com",
                wsd.community.domain.user.entity.UserRole.USER);

        // when
        authService.logout(accessToken);

        // then
        // Access Token 블랙리스트 처리 검증
        verify(redisService).setValues(anyString(), anyString(), any());
        // Refresh Token 삭제 검증
        verify(redisService).deleteValues(anyString());
    }
}
