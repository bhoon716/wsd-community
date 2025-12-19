package wsd.community.domain.report.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReportUpdateRequest(
        @NotBlank(message = "신고 사유는 필수입니다.") @Size(max = 50, message = "신고 사유는 50자 이하여야 합니다.") String reason,
        @NotBlank(message = "신고 내용은 필수입니다.") @Size(max = 500, message = "신고 내용은 500자 이하여야 합니다.") String description) {
}
