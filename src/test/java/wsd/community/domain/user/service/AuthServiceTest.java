package wsd.community.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static wsd.community.redis.RedisConstant.REDIS_BL_PREFIX;
import static wsd.community.redis.RedisConstant.REDIS_RT_PREFIX;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.domain.user.request.ReissueRequest;
import wsd.community.domain.user.response.LoginResponse;
import wsd.community.redis.RedisService;
import wsd.community.security.jwt.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisService redisService;

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_success() {
        // given
        String refreshToken = "refresh_token";
        String newAccessToken = "new_access_token";
        String newRefreshToken = "new_refresh_token";
        Long userId = 1L;
        String email = "reissue@test.com";
        long refreshExpiration = 1000L;

        User user = User.builder()
                .id(userId)
                .email(email)
                .name("tester")
                .role(UserRole.USER)
                .build();

        given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
        given(jwtTokenProvider.getUserId(refreshToken)).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(redisService.getValues(REDIS_RT_PREFIX + email)).willReturn(refreshToken);
        given(jwtTokenProvider.generateAccess(userId, email, UserRole.USER)).willReturn(newAccessToken);
        given(jwtTokenProvider.generateRefresh(userId)).willReturn(newRefreshToken);
        given(jwtTokenProvider.getRefreshExpiration()).willReturn(refreshExpiration);

        // when
        LoginResponse response = authService.reissue(new ReissueRequest(refreshToken));

        // then
        assertThat(response.accessToken()).isEqualTo(newAccessToken);
        assertThat(response.refreshToken()).isEqualTo(newRefreshToken);

        verify(redisService).setValues(eq(REDIS_RT_PREFIX + email), eq(newRefreshToken), any());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() {
        // given
        String accessToken = "access_token";
        String email = "logout@test.com";
        long expiration = 1000L;

        given(jwtTokenProvider.getEmail(accessToken)).willReturn(email);
        given(jwtTokenProvider.getExpiration(accessToken)).willReturn(expiration);

        // when
        authService.logout(accessToken);

        // then
        // Refresh Token 삭제 검증
        verify(redisService).deleteValues(REDIS_RT_PREFIX + email);
        // Access Token 블랙리스트 처리 검증
        verify(redisService).setValues(eq(REDIS_BL_PREFIX + accessToken), eq("logout"),
                eq(Duration.ofMillis(expiration)));
    }
}
