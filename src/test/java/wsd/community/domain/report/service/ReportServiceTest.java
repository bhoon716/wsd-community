package wsd.community.domain.report.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
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
import wsd.community.domain.report.response.ReportDetailResponse;
import wsd.community.domain.report.response.ReportSummaryResponse;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.domain.report.handler.CommentReportTypeHandler;
import wsd.community.domain.report.handler.PostReportTypeHandler;
import wsd.community.domain.report.handler.ReportTypeHandler;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    private ReportService reportService;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).email("user@test.com").build();
        post = Post.builder().user(user).title("Title").content("Content").build();
        ReflectionTestUtils.setField(post, "id", 1L);

        PostReportTypeHandler postHandler = new PostReportTypeHandler(postRepository, reportRepository);
        CommentReportTypeHandler commentHandler = new CommentReportTypeHandler(commentRepository, reportRepository);

        List<ReportTypeHandler> handlers = List.of(postHandler, commentHandler);

        reportService = new ReportService(reportRepository, handlers);
        reportService.initHandlers();
    }

    @Test
    @DisplayName("신고 처리 - 게시글 숨김")
    void processReport_HidePost() {
        // given
        User admin = User.builder().id(3L).email("admin@test.com").role(UserRole.ADMIN)
                .build();
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        User author = User.builder().id(2L).email("author@test.com").build();
        Post post = Post.builder().user(author).title("Title").content("Content").build();

        Report report = Report.createPostReport(reporter, post, "신고 사유", "신고 내용");

        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when
        // when
        reportService.processReport(1L, ReportAction.HIDE, "규정 위반입니다.", admin);

        // then
        assertThat(post.isHidden()).isTrue();
        assertThat(report.getStatus()).isEqualTo(ReportStatus.RESOLVED);
        assertThat(report.getAction()).isEqualTo(ReportAction.HIDE);
        assertThat(report.getResolvedReason()).isEqualTo("규정 위반입니다.");
    }

    @Test
    @DisplayName("신고 처리 - 게시글 삭제")
    void processReport_DeletePost() {
        // given
        User admin = User.builder().id(3L).email("admin@test.com").role(UserRole.ADMIN)
                .build();
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        User author = User.builder().id(2L).email("author@test.com").build();
        Post post = Post.builder().user(author).title("Title").content("Content").build();

        Report report = Report.createPostReport(reporter, post, "신고 사유", "신고 내용");

        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when
        reportService.processReport(1L, ReportAction.DELETE, "삭제 사유", admin);

        // then
        verify(postRepository).delete(post);
        assertThat(report.getStatus()).isEqualTo(ReportStatus.RESOLVED);
        assertThat(report.getAction()).isEqualTo(ReportAction.DELETE);
        assertThat(report.getResolvedReason()).isEqualTo("삭제 사유");
    }

    @Test
    @DisplayName("신고 처리 - 조치 없음")
    void processReport_NoAction() {
        // given
        User admin = User.builder().id(3L).email("admin@test.com").role(UserRole.ADMIN)
                .build();
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        User author = User.builder().id(2L).email("author@test.com").build();
        Post post = Post.builder().user(author).title("Title").content("Content").build();

        Report report = Report.createPostReport(reporter, post, "신고 사유", "신고 내용");

        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when
        reportService.processReport(1L, ReportAction.NO_ACTION, "문제 없음", admin);

        // then
        assertThat(post.isHidden()).isFalse();
        assertThat(report.getStatus()).isEqualTo(ReportStatus.REJECTED);
        assertThat(report.getAction()).isEqualTo(ReportAction.NO_ACTION);
        assertThat(report.getResolvedReason()).isEqualTo("문제 없음");
    }

    @Test
    @DisplayName("신고 처리 - 동일 대상 일괄 처리")
    void processReport_BulkResolve() {
        // given
        User admin = User.builder().id(3L).email("admin@test.com").role(UserRole.ADMIN)
                .build();
        User reporter1 = User.builder().id(1L).email("r1@test.com").build();
        User reporter2 = User.builder().id(2L).email("r2@test.com").build();
        Post post = Post.builder().user(user).title("Title").content("Content").build();

        Report report1 = Report.createPostReport(reporter1, post, "Reason 1", "Desc 1");
        ReflectionTestUtils.setField(report1, "id", 1L);
        Report report2 = Report.createPostReport(reporter2, post, "Reason 2", "Desc 2");
        ReflectionTestUtils.setField(report2, "id", 2L);

        given(reportRepository.findById(1L)).willReturn(Optional.of(report1));
        given(reportRepository.findByPostAndStatusAndIdNot(post, ReportStatus.PENDING, 1L))
                .willReturn(List.of(report2));

        // when
        reportService.processReport(1L, ReportAction.HIDE, "Main reason", admin);

        // then
        assertThat(post.isHidden()).isTrue();

        // Main report
        assertThat(report1.getStatus()).isEqualTo(ReportStatus.RESOLVED);
        assertThat(report1.getAction()).isEqualTo(ReportAction.HIDE);

        // Related report
        assertThat(report2.getStatus()).isEqualTo(ReportStatus.RESOLVED);
        assertThat(report2.getAction()).isEqualTo(ReportAction.DUPLICATE);
        assertThat(report2.getResolvedReason()).contains("일괄 처리됨");
    }

    @Test
    @DisplayName("신고 처리 - NO_ACTION 시 관련 신고 유지")
    void processReport_NoAction_DoesNotAffectRelated() {
        // given
        User admin = User.builder().id(3L).role(UserRole.ADMIN).build();
        Report report1 = Report.createPostReport(user, post, "Reason 1", "Desc 1");
        ReflectionTestUtils.setField(report1, "id", 1L);

        given(reportRepository.findById(1L)).willReturn(Optional.of(report1));

        // when
        reportService.processReport(1L, ReportAction.NO_ACTION, "No problem", admin);

        // then
        assertThat(report1.getStatus()).isEqualTo(ReportStatus.REJECTED);
        assertThat(report1.getAction()).isEqualTo(ReportAction.NO_ACTION);

        // Verify repository interaction for bulk query was NOT performed
        verify(reportRepository, never()).findByPostAndStatusAndIdNot(any(), any(), any());
    }

    @Test
    @DisplayName("신고 처리 - 관리자 아님")
    void processReport_NotAdmin() {
        // given
        User normalUser = User.builder().id(1L).email("user@test.com")
                .role(wsd.community.domain.user.entity.UserRole.USER).build();

        // when & then
        assertThatThrownBy(() -> reportService.processReport(1L, ReportAction.HIDE, "reason", normalUser))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_ADMIN);
    }

    @Test
    @DisplayName("신고 삭제")
    void deleteReport() {
        // given
        Report report = Report.createPostReport(user, post, "신고 사유", "신고 내용");
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when
        reportService.cancelReport(1L, user);

        // then
        assertThat(report.getStatus()).isEqualTo(ReportStatus.CANCELED);
        assertThat(report.getCanceledAt()).isNotNull();
    }

    @Test
    @DisplayName("신고 취소 - 이미 처리됨")
    void cancelReport_NotPending() {
        // given
        Report report = Report.createPostReport(user, post, "신고 사유", "신고 내용");
        report.updateStatus(ReportStatus.RESOLVED);
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when & then
        assertThatThrownBy(() -> reportService.cancelReport(1L, user))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REPORT_ALREADY_PROCESSED);
    }

    @Test
    @DisplayName("신고 수정 - 성공")
    void updateReport_Success() {
        // given
        Report report = Report.createPostReport(user, post, "기존 신고 사유", "기존 신고 내용");
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));
        ReportUpdateRequest request = new ReportUpdateRequest("수정된 신고 사유", "수정된 신고 내용");

        // when
        ReportDetailResponse response = reportService.updateReport(1L, user, request);

        // then
        assertThat(response.reason()).isEqualTo("수정된 신고 사유");
        assertThat(response.description()).isEqualTo("수정된 신고 내용");
    }

    @Test
    @DisplayName("신고 수정 - 권한 없음")
    void updateReport_Forbidden() {
        // given
        User otherUser = User.builder().id(2L).email("other").build();
        Report report = Report.createPostReport(otherUser, post, "신고 사유", "신고 내용");
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));
        ReportUpdateRequest request = new ReportUpdateRequest("신고 사유", "신고 내용");

        // when & then
        assertThatThrownBy(() -> reportService.updateReport(1L, user, request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("신고 수정 - 대기 상태 아님")
    void updateReport_NotPending() {
        // given
        Report report = Report.createPostReport(user, post, "신고 사유", "신고 내용");
        report.updateStatus(ReportStatus.RESOLVED);
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));
        ReportUpdateRequest request = new ReportUpdateRequest("신고 사유", "신고 내용");

        // when & then
        assertThatThrownBy(() -> reportService.updateReport(1L, user, request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REPORT_ALREADY_PROCESSED);
    }

    @Test
    @DisplayName("신고 처리 - 이미 처리됨")
    void processReport_AlreadyProcessed() {
        // given
        User admin = User.builder().id(3L).role(UserRole.ADMIN).build();
        Report report = Report.createPostReport(user, post, "신고 사유", "신고 내용");
        report.updateStatus(ReportStatus.RESOLVED);

        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when & then
        assertThatThrownBy(() -> reportService.processReport(1L, ReportAction.HIDE, "reason", admin))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REPORT_ALREADY_PROCESSED);
    }

    @Test
    @DisplayName("신고 처리 - DUPLICATE 액션 불가")
    void processReport_InvalidAction_Duplicate() {
        // given
        User admin = User.builder().id(3L).role(UserRole.ADMIN).build();

        // when & then
        assertThatThrownBy(() -> reportService.processReport(1L, ReportAction.DUPLICATE, "reason", admin))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REPORT_ACTION);
    }

    @Test
    @DisplayName("전체 신고 목록 조회 - 관리자")
    void getReports_Admin() {
        // given
        User admin = User.builder().id(3L).role(UserRole.ADMIN).build();
        Pageable pageable = PageRequest.of(0, 10);
        given(reportRepository.getReports(null, null, pageable)).willReturn(Page.empty());

        // when
        reportService.getReports(admin, null, null, pageable);

        // then
        verify(reportRepository).getReports(null, null, pageable);
    }

    @Test
    @DisplayName("전체 신고 목록 조회 - 관리자 아님")
    void getReports_NotAdmin() {
        // given
        User normalUser = User.builder().id(1L).role(UserRole.USER).build();
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThatThrownBy(() -> reportService.getReports(normalUser, null, null, pageable))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_ADMIN);
    }

    @Test
    @DisplayName("내 신고 목록 조회")
    void getMyReports() {
        // given
        ReportSummaryResponse response = ReportSummaryResponse.builder()
                .reason("신고 사유")
                .build();
        Page<ReportSummaryResponse> responses = new PageImpl<>(List.of(response));
        Pageable pageable = PageRequest.of(0, 10);

        given(reportRepository.getMyReports(user, pageable)).willReturn(responses);

        // when
        Page<ReportSummaryResponse> result = reportService.getMyReports(user, pageable);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().getFirst().reason()).isEqualTo("신고 사유");
    }

    @Test
    @DisplayName("신고 상세 조회 - 성공")
    void getReport_Success() {
        // given
        ReportDetailResponse response = ReportDetailResponse.builder()
                .id(1L)
                .reporterId(1L)
                .build();

        given(reportRepository.getReportDetail(1L)).willReturn(Optional.of(response));

        // when
        ReportDetailResponse result = reportService.getReport(1L, user);

        // then
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("신고 상세 조회 - 타인의 신고 접근 불가")
    void getReport_Forbidden() {
        // given
        ReportDetailResponse response = ReportDetailResponse.builder()
                .id(1L)
                .reporterId(999L) // other user
                .build();

        given(reportRepository.getReportDetail(1L)).willReturn(Optional.of(response));

        // when & then
        assertThatThrownBy(() -> reportService.getReport(1L, user))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("신고 상세 조회 - 존재하지 않음")
    void getReport_NotFound() {
        // given
        given(reportRepository.getReportDetail(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reportService.getReport(1L, user))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REPORT_NOT_FOUND);
    }

    @Test
    @DisplayName("중복 신고 불가")
    void createReport_Duplicate() {
        // given
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        User author = User.builder().id(2L).email("author@test.com").build();
        Post post = Post.builder().user(author).build();
        ReflectionTestUtils.setField(post, "id", 1L);

        ReportCreateRequest request = new ReportCreateRequest(1L, ReportType.POST, "Reason", "Desc");

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(reportRepository.existsByReporterAndPost(reporter, post)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> reportService.createReport(reporter, request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_REPORT);
    }

    @Test
    @DisplayName("본인 게시글 신고 불가")
    void createReport_SelfReportPost() {
        // given
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        Post post = Post.builder().user(reporter).title("Title").content("Content").build();
        ReflectionTestUtils.setField(post, "id", 1L);

        ReportCreateRequest request = new ReportCreateRequest(1L, ReportType.POST, "Reason", "Desc");

        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        // when & then
        assertThatThrownBy(() -> reportService.createReport(reporter, request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SELF_REPORT_NOT_ALLOWED);
    }

    @Test
    @DisplayName("본인 댓글 신고 불가")
    void createReport_SelfReportComment() {
        // given
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        Post post = Post.builder().user(User.builder().id(2L).build()).title("Title").content("Content").build();
        Comment comment = Comment.builder().user(reporter).post(post).content("Comment content").build();
        ReflectionTestUtils.setField(comment, "id", 1L);

        ReportCreateRequest request = new ReportCreateRequest(1L, ReportType.COMMENT, "Reason", "Desc");

        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when & then
        assertThatThrownBy(() -> reportService.createReport(reporter, request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SELF_REPORT_NOT_ALLOWED);
    }
}
