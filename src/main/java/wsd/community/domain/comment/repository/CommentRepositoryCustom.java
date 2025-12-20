package wsd.community.domain.comment.repository;

import java.util.List;
import wsd.community.domain.comment.response.CommentResponse;

public interface CommentRepositoryCustom {
    List<CommentResponse> findAllByPostId(Long postId);
}
