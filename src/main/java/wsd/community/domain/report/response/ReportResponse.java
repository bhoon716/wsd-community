package wsd.community.domain.report.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import wsd.community.domain.report.entity.ReportAction;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReportResponse(
        Long id,
        Long reporterId,
        String reporterEmail,
        Long targetId,
        ReportType type,
        String reason,
        String description,
        String targetTitle,
        String targetContent,
        ReportAction action,
        ReportStatus status,
        LocalDateTime reportedAt) {

    @QueryProjection
    public ReportResponse {
    }

    public static ReportResponse from(wsd.community.domain.report.entity.Report report) {
        String title = null;
        String content = null;

        if (report.getType() == ReportType.POST) {
            title = report.getPost().getTitle();
            content = report.getPost().getContent();
        } else if (report.getType() == ReportType.COMMENT) {
            content = report.getComment().getContent();
        }

        return ReportResponse.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reporterEmail(report.getReporter().getEmail())
                .targetId(report.getType() == ReportType.POST ? report.getPost().getId() : report.getComment().getId())
                .type(report.getType())
                .reason(report.getReason())
                .description(report.getDescription())
                .targetTitle(title)
                .targetContent(content)
                .action(report.getAction())
                .status(report.getStatus())
                .reportedAt(report.getCreatedAt())
                .build();
    }
}
