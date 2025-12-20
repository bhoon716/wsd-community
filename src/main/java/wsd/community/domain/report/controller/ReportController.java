package wsd.community.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import wsd.community.common.response.CommonResponse;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.request.ReportCreateRequest;
import wsd.community.domain.report.request.ReportProcessRequest;
import wsd.community.domain.report.request.ReportUpdateRequest;
import wsd.community.domain.report.response.ReportDetailResponse;
import wsd.community.domain.report.response.ReportSummaryResponse;
import wsd.community.domain.report.service.ReportService;
import wsd.community.security.auth.CustomUserDetails;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Report", description = "신고 API")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{reportId}")
    @Operation(summary = "신고 상세 조회", description = "신고 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "신고 상세 조회 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "신고 상세 조회 성공",
                "payload": {
                    "id": 1,
                    "reporterId": 10,
                    "reporterEmail": "reporter@example.com",
                    "targetId": 100,
                    "type": "POST",
                    "reason": "부적절한 게시글",
                    "description": "욕설 포함",
                    "targetTitle": "문제의 게시글",
                    "targetContent": "문제의 내용",
                    "status": "PENDING",
                    "reportedAt": "2024-01-01T12:00:00"
                }
            }
            """)))
    public ResponseEntity<CommonResponse<ReportDetailResponse>> getReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId) {
        return CommonResponse.ok(reportService.getReport(reportId, userDetails.getUser()), "신고 상세 조회 성공");
    }

    @GetMapping("/me")
    @Operation(summary = "내 신고 목록 조회", description = "내가 신고한 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "내 신고 목록 조회 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "내 신고 목록 조회 성공",
                "payload": {
                    "content": [
                        {
                            "id": 1,
                            "reporterId": 10,
                            "reporterEmail": "reporter@example.com",
                            "targetId": 100,
                            "type": "POST",
                            "reason": "부적절한 게시글",
                            "targetTitle": "문제의 게시글",
                            "status": "PENDING",
                            "reportedAt": "2024-01-01T12:00:00"
                        }
                    ],
                    "totalElements": 1,
                    "totalPages": 1,
                    "size": 20,
                    "number": 0
                }
            }
            """)))
    public ResponseEntity<CommonResponse<Page<ReportSummaryResponse>>> getMyReports(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.ok(
                reportService.getMyReports(userDetails.getUser(), pageable),
                "내 신고 목록 조회 성공");
    }

    @PostMapping
    @Operation(summary = "신고 생성", description = "새로운 신고를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "신고 생성 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "신고 생성 성공",
                "payload": {
                    "id": 1,
                    "reporterId": 10,
                    "reporterEmail": "reporter@example.com",
                    "targetId": 100,
                    "type": "POST",
                    "reason": "부적절한 게시글",
                    "description": "욕설 포함",
                    "targetTitle": "문제의 게시글",
                    "targetContent": "문제의 내용",
                    "status": "PENDING",
                    "reportedAt": "2024-01-01T12:00:00"
                }
            }
            """)))
    public ResponseEntity<CommonResponse<ReportDetailResponse>> createReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ReportCreateRequest request) {
        ReportDetailResponse response = reportService.createReport(userDetails.getUser(), request);
        return CommonResponse.created(response, URI.create("/api/reports/" + response.id()), "신고 생성 성공");
    }

    @PutMapping("/{reportId}")
    @Operation(summary = "신고 수정", description = "신고 내용을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "신고 수정 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "신고 수정 성공",
                "payload": {
                    "id": 1,
                    "reporterId": 10,
                    "reporterEmail": "reporter@example.com",
                    "targetId": 100,
                    "type": "POST",
                    "reason": "수정된 신고 사유",
                    "description": "수정된 상세 내용",
                    "targetTitle": "문제의 게시글",
                    "targetContent": "문제의 내용",
                    "status": "PENDING",
                    "reportedAt": "2024-01-01T12:00:00"
                }
            }
            """)))
    public ResponseEntity<CommonResponse<ReportDetailResponse>> updateReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId,
            @Valid @RequestBody ReportUpdateRequest request) {
        return CommonResponse.ok(reportService.updateReport(reportId, userDetails.getUser(), request), "신고 수정 성공");
    }

    @DeleteMapping("/{reportId}")
    @Operation(summary = "신고 취소", description = "신고를 취소합니다.")
    @ApiResponse(responseCode = "204", description = "취소 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "신고 취소 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "신고 취소 성공",
                "payload": null
            }
            """)))
    public ResponseEntity<CommonResponse<Void>> cancelReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId) {
        reportService.cancelReport(reportId, userDetails.getUser());
        return CommonResponse.noContent("신고 취소 성공");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @Operation(summary = "전체 신고 목록 조회 (관리자 전용)", description = "전체 신고 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "전체 신고 목록 조회 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "신고 목록 조회 성공",
                "payload": {
                    "content": [
                        {
                            "id": 1,
                            "reporterId": 10,
                            "reporterEmail": "reporter@example.com",
                            "targetId": 100,
                            "type": "POST",
                            "reason": "부적절한 게시글",
                            "targetTitle": "문제의 게시글",
                            "status": "PENDING",
                            "reportedAt": "2024-01-01T12:00:00"
                        }
                    ],
                    "totalElements": 1,
                    "totalPages": 1,
                    "size": 20,
                    "number": 0
                }
            }
            """)))
    public ResponseEntity<CommonResponse<Page<ReportSummaryResponse>>> getReports(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) ReportType type,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.ok(reportService.getReports(userDetails.getUser(), status, type, pageable),
                "신고 목록 조회 성공");
    }

    @PostMapping("/{reportId}/process")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @Operation(summary = "신고 처리 (관리자 전용)", description = "신고를 처리합니다.")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "신고 처리 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "신고 처리 성공",
                "payload": {
                    "id": 1,
                    "reporterId": 10,
                    "reporterEmail": "reporter@example.com",
                    "targetId": 100,
                    "type": "POST",
                    "reason": "부적절한 게시글",
                    "description": "욕설 포함",
                    "targetTitle": "문제의 게시글",
                    "targetContent": "문제의 내용",
                    "resolvedReason": "확인 후 차단 조치",
                    "resolvedByEmail": "admin@example.com",
                    "resolvedAt": "2024-01-02T10:00:00",
                    "action": "BAN_USER",
                    "status": "RESOLVED",
                    "reportedAt": "2024-01-01T12:00:00"
                }
            }
            """)))
    public ResponseEntity<CommonResponse<ReportDetailResponse>> processReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId,
            @Valid @RequestBody ReportProcessRequest request) {

        return CommonResponse.ok(
                reportService.processReport(reportId, request.action(), request.reason(), userDetails.getUser()),
                "신고 처리 성공");
    }
}