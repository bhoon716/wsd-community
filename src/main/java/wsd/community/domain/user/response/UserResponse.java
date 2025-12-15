package wsd.community.domain.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wsd.community.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@Schema(description = "사용자 정보 응답 DTO")
public class UserResponse {

    @Schema(description = "사용자 ID", example = "10")
    private Long id;

    @Schema(description = "이메일", example = "hong@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "역할 (USER/ADMIN)", example = "USER")
    private String role;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name());
    }
}
