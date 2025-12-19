package wsd.community.domain.report.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import wsd.community.domain.report.entity.ReportType;

public record ReportCreateRequest(
        @NotNull(message = "신고 대상 ID는 필수입니다.") Long targetId,

        @NotNull(message = "신고 유형은 필수입니다.") ReportType type,

        @NotBlank(message = "신고 사유는 필수입니다.") @Size(max = 50, message = "신고 사유는 50자 이하여야 합니다.") String reason,

        @Size(max = 500, message = "신고 상세 내용은 500자 이하여야 합니다.") String description) {
}
