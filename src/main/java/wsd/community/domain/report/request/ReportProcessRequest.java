package wsd.community.domain.report.request;

import jakarta.validation.constraints.NotNull;
import wsd.community.domain.report.entity.ReportAction;

public record ReportProcessRequest(
        @NotNull(message = "조치 내용을 선택해주세요.") ReportAction action,

        String reason) {
}
