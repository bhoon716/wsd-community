package wsd.community.domain.user.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import wsd.community.domain.user.entity.UserRole;

public record UserRoleChangeRequest(@NotNull(message = "변경할 권한은 필수입니다.") UserRole role) {

    @AssertTrue(message = "OWNER 권한으로 변경할 수 없습니다.")
    public boolean isNotOwner() {
        return role != UserRole.OWNER;
    }
}
