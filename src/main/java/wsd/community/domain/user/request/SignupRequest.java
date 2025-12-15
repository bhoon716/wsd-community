package wsd.community.domain.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 요청 DTO")
public class SignupRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Schema(description = "이메일", example = "newuser@example.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Length(min = 9, max = 20, message = "비밀번호는 최소 9자, 최대 20자 입니다.")
    @Schema(description = "비밀번호 (9~20자)", example = "password123")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    @Length(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
    @Schema(description = "이름 (2~10자)", example = "이영희")
    private String name;
}
