package wsd.community.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static wsd.community.security.jwt.JwtConstant.REDIS_RT_PREFIX;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import wsd.community.common.error.ErrorCode;
import wsd.community.common.redis.RedisService;
import wsd.community.common.response.CustomException;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.domain.user.request.LoginRequest;
import wsd.community.domain.user.request.ReissueRequest;
import wsd.community.domain.user.request.SignupRequest;
import wsd.community.domain.user.response.LoginResponse;
import wsd.community.domain.user.response.SignupResponse;
import wsd.community.security.jwt.JwtTokenProvider;

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

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // given
        SignupRequest request = new SignupRequest("test@test.com", "password123", "tester");

        // when
        SignupResponse response = authService.signup(request);

        // then
        assertThat(response.getUserId()).isNotNull();
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getName()).isEqualTo(request.getName());

        User savedUser = userRepository.findById(response.getUserId()).orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void signup_fail_duplicate_email() {
        // given
        SignupRequest request1 = new SignupRequest("duplicate@test.com", "password123", "tester1");
        authService.signup(request1);

        SignupRequest request2 = new SignupRequest("duplicate@test.com", "password123", "tester2");

        // when & then
        assertThatThrownBy(() -> authService.signup(request2))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        authService.signup(new SignupRequest("login@test.com", "password123", "tester"));
        LoginRequest request = new LoginRequest("login@test.com", "password123");

        // when
        LoginResponse response = authService.login(request);

        // then
        assertThat(response.getUser().getEmail()).isEqualTo("login@test.com");
        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();

        // Redis 저장 검증 (Mock)
        verify(redisService).setValues(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_fail_not_found_email() {
        // given
        LoginRequest request = new LoginRequest("unknown@test.com", "password123");

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_USER);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_wrong_password() {
        // given
        authService.signup(new SignupRequest("wrongpw@test.com", "password123", "tester"));
        LoginRequest request = new LoginRequest("wrongpw@test.com", "wrongpassword");

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_success() {
        // given
        SignupResponse signup = authService.signup(new SignupRequest("reissue@test.com", "password123", "tester"));
        String refreshToken = jwtTokenProvider.generateRefresh(signup.getUserId());

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
