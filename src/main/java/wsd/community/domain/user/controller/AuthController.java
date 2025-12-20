package wsd.community.domain.user.controller;

import static wsd.community.security.config.SecurityConstant.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
            @RequestHeader(AUTHORIZATION) String accessToken,
            HttpServletResponse httpServletResponse) {
        authService.logout(accessToken.substring(TOKEN_PREFIX.length()));
        removeRefreshTokenCookie(httpServletResponse);
        return CommonResponse.ok(null, "로그아웃 성공");
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
        LoginResponse response = authService.loginWithFirebase(request.idToken());

        setAccessTokenHeader(httpServletResponse, response.accessToken());
        addRefreshTokenCookie(httpServletResponse, response.refreshToken());

        return CommonResponse.ok(response.user(), "로그인 성공");
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
            @CookieValue(REFRESH_TOKEN) String refreshToken,
            HttpServletResponse httpServletResponse) {
        LoginResponse response = authService.reissue(new ReissueRequest(refreshToken));

        setAccessTokenHeader(httpServletResponse, response.accessToken());
        addRefreshTokenCookie(httpServletResponse, response.refreshToken());

        return CommonResponse.ok(response.user(), "토큰 재발급 성공");
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(AUTHORIZATION, TOKEN_PREFIX + accessToken);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

    private void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
