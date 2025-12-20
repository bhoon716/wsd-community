package wsd.community.domain.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Firebase 로그인 요청 DTO")
public record FirebaseLoginRequest(
        @NotBlank(message = "ID 토큰은 필수입니다") @Schema(description = "Firebase ID Token", example = "eyJhbGciOiJSUzI1NiIsImt...") String idToken) {
}
