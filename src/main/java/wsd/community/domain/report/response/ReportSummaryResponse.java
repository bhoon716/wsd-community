package wsd.community.domain.report.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReportSummaryResponse(
        Long id,
        Long reporterId,
        String reporterEmail,
        Long targetId,
        ReportType type,
        String reason,
        String targetTitle,
        ReportStatus status,
        LocalDateTime reportedAt) {

    @QueryProjection
    public ReportSummaryResponse {
    }

    public static ReportSummaryResponse from(Report report) {
        return ReportSummaryResponse.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reporterEmail(report.getReporter().getEmail())
                .targetId(report.getTargetId())
                .type(report.getType())
                .reason(report.getReason())
                .targetTitle(report.getTargetTitle())
                .status(report.getStatus())
                .reportedAt(report.getCreatedAt())
                .build();
    }
}
