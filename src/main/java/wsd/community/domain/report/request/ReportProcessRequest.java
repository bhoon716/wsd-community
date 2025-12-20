package wsd.community.domain.report.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import wsd.community.domain.report.entity.ReportAction;

public record ReportProcessRequest(
                @NotNull(message = "조치 내용을 선택해주세요.") ReportAction action,

                @Size(max = 500, message = "처리 사유는 500자 이하여야 합니다.") String reason) {
}
