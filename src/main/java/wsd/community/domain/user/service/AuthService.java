package wsd.community.domain.user.service;

import static wsd.community.security.config.SecurityConstant.LOGOUT_VALUE;
import static wsd.community.redis.RedisConstant.REDIS_BL_PREFIX;
import static wsd.community.redis.RedisConstant.REDIS_RT_PREFIX;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.security.jwt.JwtTokenProvider;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.domain.user.request.ReissueRequest;
import wsd.community.domain.user.response.LoginResponse;
import wsd.community.domain.user.response.UserResponse;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.redis.RedisService;
import com.google.firebase.auth.FirebaseToken;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final FirebaseService firebaseService;

    public LoginResponse loginWithFirebase(String idToken) {
        FirebaseToken decodedToken = firebaseService.verifyToken(idToken);
        String email = decodedToken.getEmail();
        String uid = decodedToken.getUid();
        String name = decodedToken.getName() != null ? decodedToken.getName() : email.split("@")[0];

        User user = userRepository.findByEmail(email)
                .map(entity -> {
                    entity.updateProfile(name);
                    return entity;
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .name(name)
                        .role(UserRole.USER)
                        .provider("firebase")
                        .providerId(uid)
                        .build()));

        return generateLoginResponse(user);
    }

    public void logout(String accessToken) {
        String email = jwtTokenProvider.getEmail(accessToken);
        log.info("로그아웃 요청: email={}", email);
        redisService.deleteValues(REDIS_RT_PREFIX + email);

        long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisService.setValues(REDIS_BL_PREFIX + accessToken, LOGOUT_VALUE, Duration.ofMillis(expiration));
        log.info("로그아웃 완료: email={}", email);
    }

    public LoginResponse reissue(ReissueRequest request) {
        log.info("토큰 재발급 요청");
        String refreshToken = request.refreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        validateRefreshToken(user.getEmail(), refreshToken);

        log.info("토큰 재발급 완료: userId={}", userId);
        return generateLoginResponse(user);
    }

    private LoginResponse generateLoginResponse(User user) {
        String accessToken = jwtTokenProvider.generateAccess(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefresh(user.getId());

        redisService.setValues(REDIS_RT_PREFIX + user.getEmail(), refreshToken,
                Duration.ofMillis(jwtTokenProvider.getRefreshExpiration()));

        UserResponse userResponse = UserResponse.from(user);
        return LoginResponse.of(accessToken, refreshToken, userResponse);
    }

    private void validateRefreshToken(String email, String refreshToken) {
        String storedRefreshToken = redisService.getValues(REDIS_RT_PREFIX + email);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}
