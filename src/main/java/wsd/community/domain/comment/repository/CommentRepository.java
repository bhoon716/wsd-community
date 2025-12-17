package wsd.community.domain.comment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wsd.community.domain.comment.entity.Comment;
import wsd.community.domain.post.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
}
