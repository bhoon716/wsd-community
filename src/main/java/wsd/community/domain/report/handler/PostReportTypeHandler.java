package wsd.community.domain.report.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.repository.ReportRepository;
import wsd.community.domain.report.request.ReportCreateRequest;
import wsd.community.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class PostReportTypeHandler implements ReportTypeHandler {

    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    @Override
    public ReportType getType() {
        return ReportType.POST;
    }

    @Override
    public Report createReport(User reporter, ReportCreateRequest request) {
        Post post = postRepository.findById(request.targetId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (reporter.getId().equals(post.getUser().getId())) {
            throw new CustomException(ErrorCode.SELF_REPORT_NOT_ALLOWED);
        }

        if (reportRepository.existsByReporterAndPost(reporter, post)) {
            throw new CustomException(ErrorCode.DUPLICATE_REPORT);
        }

        return Report.createPostReport(reporter, post, request.reason(), request.description());
    }

    @Override
    public void hideTarget(Report report) {
        report.getPost().hide();
    }

    @Override
    public void deleteTarget(Report report) {
        postRepository.delete(report.getPost());
    }

    @Override
    public List<Report> getRelatedPendingReports(Report report) {
        return reportRepository.findByPostAndStatusAndIdNot(report.getPost(), ReportStatus.PENDING, report.getId());
    }
}
