package wsd.community.domain.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wsd.community.common.response.CommonResponse;
import wsd.community.domain.user.request.FirebaseLoginRequest;
import wsd.community.domain.user.request.ReissueRequest;
import wsd.community.domain.user.response.LoginResponse;
import wsd.community.domain.user.response.UserResponse;
import wsd.community.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "로그인 / 로그아웃 / 토큰 재발급")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days

    private final AuthService authService;

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 처리합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "로그아웃 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "로그아웃 성공",
                "payload": null
            }
            """)))
    public ResponseEntity<CommonResponse<Void>> logout(
            @RequestHeader(AUTHORIZATION_HEADER) String accessToken,
            HttpServletResponse httpServletResponse) {
        authService.logout(accessToken.substring(BEARER_PREFIX.length()));
        removeRefreshTokenCookie(httpServletResponse);
        return ResponseEntity.ok(CommonResponse.success(null, "로그아웃 성공"));
    }

    @PostMapping("/firebase")
    @Operation(summary = "Firebase 로그인", description = "Firebase ID Token을 사용하여 로그인합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "로그인 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "로그인 성공",
                "payload": {
                    "userId": 1,
                    "email": "user@example.com",
                    "name": "홍길동"
                }
            }
            """)))
    public ResponseEntity<CommonResponse<UserResponse>> loginWithFirebase(
            @RequestBody @Valid FirebaseLoginRequest request,
            HttpServletResponse httpServletResponse) {
        LoginResponse response = authService.loginWithFirebase(request.getIdToken());

        setAccessTokenHeader(httpServletResponse, response.getAccessToken());
        addRefreshTokenCookie(httpServletResponse, response.getRefreshToken());

        return ResponseEntity.ok(CommonResponse.success(response.getUser(), "로그인 성공"));
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "Refresh Token을 사용하여 Access Token을 재발급합니다.")
    @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "토큰 재발급 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "토큰 재발급 성공",
                "payload": {
                     "userId": 1,
                    "email": "user@example.com",
                    "name": "홍길동"
                }
            }
            """)))
    public ResponseEntity<CommonResponse<UserResponse>> reissue(
            @CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse httpServletResponse) {
        LoginResponse response = authService.reissue(new ReissueRequest(refreshToken));

        setAccessTokenHeader(httpServletResponse, response.getAccessToken());
        addRefreshTokenCookie(httpServletResponse, response.getRefreshToken());

        return ResponseEntity.ok(CommonResponse.success(response.getUser(), "토큰 재발급 성공"));
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

    private void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
