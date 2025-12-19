package wsd.community.domain.report.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
import wsd.community.domain.report.response.ReportResponse;
import wsd.community.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).email("user@test.com").build();
        post = Post.builder().user(user).title("Title").content("Content").build();
        ReflectionTestUtils.setField(post, "id", 1L);
    }

    @Test
    @DisplayName("신고 처리 - 게시글 숨김")
    void processReport_HidePost() {
        // given
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        User author = User.builder().id(2L).email("author@test.com").build();
        Post post = Post.builder().user(author).title("Title").content("Content").build();

        Report report = Report.createPostReport(reporter, post, "신고 사유", "신고 내용");

        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when
        reportService.processReport(1L, ReportAction.HIDE);

        // then
        assertThat(post.isHidden()).isTrue();
        assertThat(report.getStatus()).isEqualTo(ReportStatus.RESOLVED);
        assertThat(report.getAction()).isEqualTo(ReportAction.HIDE);
    }

    @Test
    @DisplayName("신고 처리 - 게시글 삭제")
    void processReport_DeletePost() {
        // given
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        User author = User.builder().id(2L).email("author@test.com").build();
        Post post = Post.builder().user(author).title("Title").content("Content").build();

        Report report = Report.createPostReport(reporter, post, "신고 사유", "신고 내용");

        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when
        reportService.processReport(1L, ReportAction.DELETE);

        // then
        verify(postRepository).delete(post);
        assertThat(report.getStatus()).isEqualTo(ReportStatus.RESOLVED);
        assertThat(report.getAction()).isEqualTo(ReportAction.DELETE);
    }

    @Test
    @DisplayName("신고 처리 - 조치 없음")
    void processReport_NoAction() {
        // given
        User reporter = User.builder().id(1L).email("reporter@test.com").build();
        User author = User.builder().id(2L).email("author@test.com").build();
        Post post = Post.builder().user(author).title("Title").content("Content").build();

        Report report = Report.createPostReport(reporter, post, "신고 사유", "신고 내용");

        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when
        reportService.processReport(1L, ReportAction.NO_ACTION);

        // then
        assertThat(post.isHidden()).isFalse();
        assertThat(report.getStatus()).isEqualTo(ReportStatus.REJECTED);
        assertThat(report.getAction()).isEqualTo(ReportAction.NO_ACTION);
    }

    @Test
    @DisplayName("신고 삭제")
    void deleteReport() {
        // given
        Report report = Report.createPostReport(user, post, "신고 사유", "신고 내용");
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        // when
        reportService.deleteReport(1L, user);

        // then
        verify(reportRepository).delete(report);
    }

    @Test
    @DisplayName("신고 수정 - 성공")
    void updateReport_Success() {
        // given
        Report report = Report.createPostReport(user, post, "기존 신고 사유", "기존 신고 내용");
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));
        ReportUpdateRequest request = new ReportUpdateRequest("수정된 신고 사유", "수정된 신고 내용");

        // when
        ReportResponse response = reportService.updateReport(1L, user, request);

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
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("내 신고 목록 조회")
    void getMyReports() {
        // given
        Report report = Report.createPostReport(user, post, "신고 사유", "신고 내용");
        Page<Report> reports = new PageImpl<>(List.of(report));
        Pageable pageable = PageRequest.of(0, 10);

        given(reportRepository.findByReporter(user, pageable)).willReturn(reports);

        // when
        Page<ReportResponse> responses = reportService.getMyReports(user, pageable);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.getContent().getFirst().reason()).isEqualTo("신고 사유");
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
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
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
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }
}
