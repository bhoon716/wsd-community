package wsd.community.domain.comment.repository;

import java.time.LocalDateTime;
import java.util.List;
import wsd.community.domain.comment.response.CommentResponse;
import wsd.community.domain.stats.response.TopCommenterResponse;

public interface CommentRepositoryCustom {

    List<CommentResponse> findAllByPostId(Long postId);

    List<TopCommenterResponse> findTopCommenters(LocalDateTime startDate, LocalDateTime endDate, int limit);
}
