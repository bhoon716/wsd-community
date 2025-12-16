package wsd.community.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import wsd.community.common.error.ErrorCode;
import wsd.community.common.response.CustomException;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.redis.RedisConstant;
import wsd.community.redis.RedisService;
import wsd.community.security.config.SecurityConstant;
import wsd.community.security.jwt.JwtTokenProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private final JwtTokenProvider jwtTokenProvider;
        private final RedisService redisService;
        private final UserRepository userRepository;

        @Value("${app.oauth2.redirect-uri.success}")
        private String redirectUri;

        @Override
        public void onAuthenticationSuccess(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                String email = (String) oAuth2User.getAttributes().get(SecurityConstant.CLAIM_EMAIL);

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

                String accessToken = jwtTokenProvider.generateAccess(user.getId(), user.getEmail(), user.getRole());
                String refreshToken = jwtTokenProvider.generateRefresh(user.getId());

                redisService.setValues(RedisConstant.REDIS_RT_PREFIX + user.getEmail(), refreshToken,
                                Duration.ofMillis(jwtTokenProvider.getRefreshExpiration()));

                addRefreshTokenCookie(response, refreshToken);

                response.addHeader(SecurityConstant.AUTHORIZATION, SecurityConstant.TOKEN_PREFIX + accessToken);

                log.info("OAuth2 로그인 성공: email={}", email);
                getRedirectStrategy().sendRedirect(request, response, redirectUri);
        }

        private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
                Cookie cookie = new Cookie(SecurityConstant.REFRESH_TOKEN, refreshToken);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge((int) (jwtTokenProvider.getRefreshExpiration() / 1000));
                response.addCookie(cookie);
        }
}
