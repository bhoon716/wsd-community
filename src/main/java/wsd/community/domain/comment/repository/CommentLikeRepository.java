package wsd.community.domain.comment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wsd.community.domain.comment.entity.Comment;
import wsd.community.domain.comment.entity.CommentLike;
import wsd.community.domain.user.entity.User;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByUserAndComment(User user, Comment comment);

    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
