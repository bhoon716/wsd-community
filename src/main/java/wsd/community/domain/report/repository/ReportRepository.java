package wsd.community.domain.report.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wsd.community.domain.comment.entity.Comment;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.user.entity.User;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {

    boolean existsByReporterAndPost(User reporter, Post post);

    boolean existsByReporterAndComment(User reporter, Comment comment);

    Page<Report> findByReporter(User reporter, Pageable pageable);

    List<Report> findByPostAndStatus(Post post, ReportStatus status);

    List<Report> findByPostAndStatusAndIdNot(Post post, ReportStatus status, Long id);

    List<Report> findByCommentAndStatus(Comment comment, ReportStatus status);

    List<Report> findByCommentAndStatusAndIdNot(Comment comment, ReportStatus status, Long id);
}
