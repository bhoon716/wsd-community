package wsd.community.domain.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@Schema(description = "로그인 응답 DTO")
public class LoginResponse {
    @Schema(description = "Access Token")
    private String accessToken;

    @Schema(description = "Refresh Token")
    private String refreshToken;

    @Schema(description = "사용자 정보")
    private UserResponse user;

    public static LoginResponse of(String accessToken, String refreshToken, UserResponse user) {
        return new LoginResponse(accessToken, refreshToken, user);
    }
}
