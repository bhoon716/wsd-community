package wsd.community.domain.report.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import wsd.community.common.audit.BaseEntity;
import wsd.community.domain.comment.entity.Comment;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE reports SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "reports", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "reporter_id", "post_id" }),
        @UniqueConstraint(columnNames = { "reporter_id", "comment_id" })
})
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(nullable = false, length = 50)
    private String reason;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportAction action;

    @Column(length = 500)
    private String resolvedReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private User resolvedBy;

    private java.time.LocalDateTime resolvedAt;

    private java.time.LocalDateTime canceledAt;

    @Column(length = 300)
    private String targetTitle;

    @Column(columnDefinition = "TEXT")
    private String targetContent;

    @Builder
    private Report(User reporter, Post post, Comment comment, String reason, String description, ReportType type,
            ReportAction action, String targetTitle, String targetContent) {
        this.reporter = reporter;
        this.post = post;
        this.comment = comment;
        this.reason = reason;
        this.description = description;
        this.status = ReportStatus.PENDING;
        this.type = type;
        this.action = action != null ? action : ReportAction.NO_ACTION;
        this.targetTitle = targetTitle;
        this.targetContent = targetContent;
    }

    public static Report createPostReport(User reporter, Post post, String reason, String description) {
        return Report.builder()
                .reporter(reporter)
                .post(post)
                .type(ReportType.POST)
                .reason(reason)
                .description(description)
                .action(ReportAction.NO_ACTION)
                .targetTitle(post.getTitle())
                .targetContent(post.getContent())
                .build();
    }

    public static Report createCommentReport(User reporter, Comment comment, String reason, String description) {
        return Report.builder()
                .reporter(reporter)
                .comment(comment)
                .type(ReportType.COMMENT)
                .reason(reason)
                .description(description)
                .action(ReportAction.NO_ACTION)
                .targetContent(comment.getContent())
                .build();
    }

    public void updateStatus(ReportStatus status) {
        this.status = status;
    }

    public void resolve(ReportAction action, String resolvedReason, User resolver) {
        this.action = action;
        this.resolvedReason = resolvedReason;
        this.resolvedBy = resolver;
        this.resolvedAt = java.time.LocalDateTime.now();

        if (action == ReportAction.HIDE || action == ReportAction.DELETE || action == ReportAction.DUPLICATE) {
            this.status = ReportStatus.RESOLVED;
        } else if (action == ReportAction.NO_ACTION) {
            this.status = ReportStatus.REJECTED;
        }
    }

    public void resolveAsDuplicate(ReportAction primaryAction, User resolver) {
        this.resolve(
                ReportAction.DUPLICATE,
                "동일 대상에 대한 " + primaryAction + " 조치로 인해 일괄 처리됨",
                resolver);
    }

    public void cancel() {
        this.status = ReportStatus.CANCELED;
        this.canceledAt = java.time.LocalDateTime.now();
    }

    public void update(String reason, String description) {
        this.reason = reason;
        this.description = description;
    }

    public Long getTargetId() {
        return this.type == ReportType.POST ? this.post.getId() : this.comment.getId();
    }

    public String getTargetTitle() {
        return this.targetTitle;
    }

    public String getTargetContent() {
        return this.targetContent;
    }
}
