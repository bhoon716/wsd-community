package wsd.community.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.comment.entity.Comment;
import wsd.community.domain.comment.repository.CommentRepository;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportAction;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.repository.ReportRepository;
import wsd.community.domain.report.request.ReportCreateRequest;
import wsd.community.domain.report.request.ReportUpdateRequest;
import wsd.community.domain.report.response.ReportResponse;
import wsd.community.domain.user.entity.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Page<ReportResponse> getReports(ReportStatus status, ReportType type, Pageable pageable) {
        return reportRepository.getReports(status, type, pageable);
    }

    public Page<ReportResponse> getMyReports(User user, Pageable pageable) {
        return reportRepository.findByReporter(user, pageable)
                .map(ReportResponse::from);
    }

    public ReportResponse getReport(Long reportId, User currentUser) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        if (!report.getReporter().getId().equals(currentUser.getId())
                && currentUser.getRole() != wsd.community.domain.user.entity.UserRole.ADMIN) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return ReportResponse.from(report);
    }

    @Transactional
    public ReportResponse createReport(User reporter, ReportCreateRequest request) {
        Report report = null;

        if (request.type() == ReportType.POST) {
            Post post = postRepository.findById(request.targetId())
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

            if (post.getUser().getId().equals(reporter.getId())) {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }

            if (reportRepository.existsByReporterAndPost(reporter, post)) {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }

            report = Report.createPostReport(reporter, post, request.reason(), request.description());
        } else if (request.type() == ReportType.COMMENT) {
            Comment comment = commentRepository.findById(request.targetId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

            if (comment.getUser().getId().equals(reporter.getId())) {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }

            if (reportRepository.existsByReporterAndComment(reporter, comment)) {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }

            report = Report.createCommentReport(reporter, comment, request.reason(), request.description());
        }

        if (report == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        Report savedReport = reportRepository.save(report);
        return ReportResponse.from(savedReport);
    }

    @Transactional
    public ReportResponse processReport(Long reportId, ReportAction action) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        switch (action) {
            case HIDE:
                if (report.getType() == ReportType.POST) {
                    report.getPost().hide();
                } else if (report.getType() == ReportType.COMMENT) {
                    report.getComment().hide();
                }
                report.updateStatus(ReportStatus.RESOLVED);
                break;
            case DELETE:
                if (report.getType() == ReportType.POST) {
                    postRepository.delete(report.getPost());
                } else if (report.getType() == ReportType.COMMENT) {
                    commentRepository.delete(report.getComment());
                }
                report.updateStatus(ReportStatus.RESOLVED);
                break;
            case NO_ACTION:
                report.updateStatus(ReportStatus.REJECTED);
                break;
        }

        report.updateAction(action);
        return ReportResponse.from(report);
    }

    @Transactional
    public ReportResponse updateReport(Long reportId, User currentUser, ReportUpdateRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        if (!report.getReporter().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        report.update(request.reason(), request.description());
        return ReportResponse.from(report);
    }

    @Transactional
    public void deleteReport(Long reportId, User currentUser) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        if (!report.getReporter().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        reportRepository.delete(report);
    }
}
