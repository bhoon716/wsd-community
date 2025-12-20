package wsd.community.domain.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import wsd.community.domain.user.entity.UserRole;

@Schema(description = "회원 역할 변경 요청 DTO (OWNER 전용)")
public record UserRoleChangeRequest(
        @NotNull(message = "변경할 권한은 필수입니다.") @Schema(description = "변경할 역할 (ADMIN, USER)", example = "ADMIN") UserRole role) {

    @AssertTrue(message = "OWNER 권한으로 변경할 수 없습니다.")
    public boolean isNotOwner() {
        return role != UserRole.OWNER;
    }
}
