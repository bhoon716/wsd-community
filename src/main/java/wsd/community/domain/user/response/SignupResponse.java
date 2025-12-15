package wsd.community.domain.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wsd.community.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@Schema(description = "회원가입 응답 DTO")
public class SignupResponse {
    @Schema(description = "사용자 ID", example = "10")
    private Long userId;

    @Schema(description = "이메일", example = "newuser@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "이영희")
    private String name;

    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getId(),
                user.getEmail(),
                user.getName());
    }
}
