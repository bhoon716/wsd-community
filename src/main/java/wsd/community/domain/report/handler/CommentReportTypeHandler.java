package wsd.community.domain.report.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.comment.entity.Comment;
import wsd.community.domain.comment.repository.CommentRepository;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.repository.ReportRepository;
import wsd.community.domain.report.request.ReportCreateRequest;
import wsd.community.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class CommentReportTypeHandler implements ReportTypeHandler {

    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    @Override
    public ReportType getType() {
        return ReportType.COMMENT;
    }

    @Override
    public Report createReport(User reporter, ReportCreateRequest request) {
        Comment comment = commentRepository.findById(request.targetId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (reporter.getId().equals(comment.getUser().getId())) {
            throw new CustomException(ErrorCode.SELF_REPORT_NOT_ALLOWED);
        }

        if (reportRepository.existsByReporterAndComment(reporter, comment)) {
            throw new CustomException(ErrorCode.DUPLICATE_REPORT);
        }

        return Report.createCommentReport(reporter, comment, request.reason(), request.description());
    }

    @Override
    public void hideTarget(Report report) {
        report.getComment().hide();
    }

    @Override
    public void deleteTarget(Report report) {
        commentRepository.delete(report.getComment());
    }

    @Override
    public List<Report> getRelatedPendingReports(Report report) {
        return reportRepository.findByCommentAndStatusAndIdNot(report.getComment(), ReportStatus.PENDING,
                report.getId());
    }
}
