package wsd.community.domain.post.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.entity.PostLike;
import wsd.community.domain.user.entity.User;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUserAndPost(User user, Post post);
}
