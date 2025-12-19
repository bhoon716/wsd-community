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
@Table(name = "reports")
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

    @Builder
    private Report(User reporter, Post post, Comment comment, String reason, String description, ReportType type,
            ReportAction action) {
        this.reporter = reporter;
        this.post = post;
        this.comment = comment;
        this.reason = reason;
        this.description = description;
        this.status = ReportStatus.PENDING;
        this.type = type;
        this.action = action != null ? action : ReportAction.NO_ACTION;
    }

    public static Report createPostReport(User reporter, Post post, String reason, String description) {
        return Report.builder()
                .reporter(reporter)
                .post(post)
                .type(ReportType.POST)
                .reason(reason)
                .description(description)
                .action(ReportAction.NO_ACTION)
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
                .build();
    }

    public void updateStatus(ReportStatus status) {
        this.status = status;
    }

    public void updateAction(ReportAction action) {
        this.action = action;
    }

    public void update(String reason, String description) {
        this.reason = reason;
        this.description = description;
    }
}
