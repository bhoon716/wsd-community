package wsd.community.domain.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import wsd.community.domain.user.entity.User;

@Schema(description = "사용자 정보 응답 DTO")
public record UserResponse(
        @Schema(description = "사용자 ID", example = "10") Long id,
        @Schema(description = "이메일", example = "hong@example.com") String email,
        @Schema(description = "사용자 이름", example = "홍길동") String name,
        @Schema(description = "역할 (USER/ADMIN)", example = "USER") String role) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name());
    }
}
