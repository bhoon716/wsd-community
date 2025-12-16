package wsd.community.security.oauth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static wsd.community.redis.RedisConstant.REDIS_RT_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.redis.RedisService;
import wsd.community.security.jwt.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class OAuth2SuccessHandlerTest {

    @InjectMocks
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisService redisService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    @Test
    @DisplayName("OAuth2 핸들러 로그인 성공")
    void onAuthenticationSuccess_success() throws IOException, Exception {
        // Given
        String email = "test@example.com";
        String accessToken = "access.token.value";
        String refreshToken = "refresh.token.value";
        String redirectUri = "http://localhost:3000/oauth/callback";

        ReflectionTestUtils.setField(oAuth2SuccessHandler, "redirectUri", redirectUri);

        given(authentication.getPrincipal()).willReturn(oAuth2User);
        given(oAuth2User.getAttributes()).willReturn(Map.of("email", email));

        User user = User.builder()
                .id(1L)
                .email(email)
                .role(UserRole.USER)
                .name("Test User")
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(jwtTokenProvider.generateAccess(any(), any(), any())).willReturn(accessToken);
        given(jwtTokenProvider.generateRefresh(any())).willReturn(refreshToken);
        given(jwtTokenProvider.getRefreshExpiration()).willReturn(1000L);
        given(response.encodeRedirectURL(anyString())).willReturn(redirectUri);

        // When
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // Then
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        verify(redisService).setValues(keyCaptor.capture(), valueCaptor.capture(), durationCaptor.capture());

        assertThat(keyCaptor.getValue()).isEqualTo(REDIS_RT_PREFIX + email);
        assertThat(valueCaptor.getValue()).isEqualTo(refreshToken);
        assertThat(durationCaptor.getValue()).isEqualTo(Duration.ofMillis(1000L));

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(urlCaptor.capture());

        String redirectUrl = urlCaptor.getValue();
        assertThat(redirectUrl).isEqualTo(redirectUri);
        assertThat(redirectUrl).doesNotContain("accessToken=");
        assertThat(redirectUrl).doesNotContain("refreshToken=");

        // Verify Cookie
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie cookie = cookieCaptor.getValue();
        assertThat(cookie.getName()).isEqualTo("refresh_token");
        assertThat(cookie.getValue()).isEqualTo(refreshToken);
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getSecure()).isTrue();
        assertThat(cookie.getMaxAge()).isEqualTo(1); // 1000ms / 1000 = 1s

        // Verify Header
        verify(response).addHeader("Authorization", "Bearer " + accessToken);
    }
}
