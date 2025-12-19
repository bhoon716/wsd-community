package wsd.community.domain.report.service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportAction;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.handler.ReportTypeHandler;
import wsd.community.domain.report.repository.ReportRepository;
import wsd.community.domain.report.request.ReportCreateRequest;
import wsd.community.domain.report.request.ReportUpdateRequest;
import wsd.community.domain.report.response.ReportDetailResponse;
import wsd.community.domain.report.response.ReportSummaryResponse;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.entity.UserRole;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final List<ReportTypeHandler> handlers;
    private final Map<ReportType, ReportTypeHandler> handlerMap = new java.util.EnumMap<>(ReportType.class);

    @PostConstruct
    public void initHandlers() {
        for (ReportTypeHandler handler : handlers) {
            ReportType type = handler.getType();
            if (handlerMap.containsKey(type)) {
                throw new CustomException(ErrorCode.REPORT_HANDLER_ERROR);
            }
            handlerMap.put(type, handler);
        }

        for (ReportType type : ReportType.values()) {
            if (!handlerMap.containsKey(type)) {
                throw new CustomException(ErrorCode.REPORT_HANDLER_ERROR);
            }
        }
    }

    private ReportTypeHandler getHandler(ReportType type) {
        ReportTypeHandler handler = handlerMap.get(type);
        if (handler == null) {
            throw new CustomException(ErrorCode.UNSUPPORTED_REPORT_TYPE);
        }
        return handler;
    }

    public Page<ReportSummaryResponse> getReports(User user, ReportStatus status, ReportType type, Pageable pageable) {
        validateAdmin(user);
        return reportRepository.getReports(status, type, pageable);
    }

    public Page<ReportSummaryResponse> getMyReports(User currentUser, Pageable pageable) {
        return reportRepository.getMyReports(currentUser, pageable);
    }

    public ReportDetailResponse getReport(Long reportId, User currentUser) {
        ReportDetailResponse report = reportRepository.getReportDetail(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        if (!report.reporterId().equals(currentUser.getId())
                && (currentUser.getRole() != UserRole.ADMIN)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return report;
    }

    @Transactional
    public ReportDetailResponse createReport(User reporter, ReportCreateRequest request) {
        ReportTypeHandler handler = getHandler(request.type());
        Report report = handler.createReport(reporter, request);
        reportRepository.save(report);
        return ReportDetailResponse.from(report);
    }

    @Transactional
    public ReportDetailResponse processReport(Long reportId, ReportAction action, String resolvedReason, User user) {
        validateAdmin(user);
        if (action == ReportAction.DUPLICATE) {
            throw new CustomException(ErrorCode.INVALID_REPORT_ACTION);
        }

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        validatePendingStatus(report);

        ReportTypeHandler handler = getHandler(report.getType());

        switch (action) {
            case HIDE -> handler.hideTarget(report);
            case DELETE -> handler.deleteTarget(report);
            case NO_ACTION -> {
            }
            default -> throw new CustomException(ErrorCode.INVALID_REPORT_ACTION);
        }

        report.resolve(action, resolvedReason, user);

        if (action == ReportAction.HIDE || action == ReportAction.DELETE) {
            resolveRelatedReports(handler, report, user);
        }

        return ReportDetailResponse.from(report);
    }

    private void resolveRelatedReports(ReportTypeHandler handler, Report primaryReport, User admin) {
        handler.getRelatedPendingReports(primaryReport)
                .forEach(related -> related.resolveAsDuplicate(primaryReport.getAction(), admin));
    }

    @Transactional
    public ReportDetailResponse updateReport(Long reportId, User currentUser, ReportUpdateRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        validateReportOwner(report, currentUser);
        validatePendingStatus(report);

        report.update(request.reason(), request.description());
        return ReportDetailResponse.from(report);
    }

    @Transactional
    public void cancelReport(Long reportId, User currentUser) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        validateReportOwner(report, currentUser);
        validatePendingStatus(report);

        report.cancel();
    }

    private void validateReportOwner(Report report, User user) {
        if (!report.getReporter().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void validatePendingStatus(Report report) {
        if (report.getStatus() != ReportStatus.PENDING) {
            throw new CustomException(ErrorCode.REPORT_ALREADY_PROCESSED);
        }
    }

    private void validateAdmin(User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }
    }
}
