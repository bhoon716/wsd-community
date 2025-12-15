package wsd.community.domain.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
@Schema(description = "비밀번호 변경 요청 DTO")
public class PasswordUpdateRequest {

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Length(min = 9, max = 20, message = "비밀번호는 최소 9자, 최대 20자 입니다.")
    @Schema(description = "새 비밀번호 (9~20자)", example = "newpassword123")
    private String password;
}
