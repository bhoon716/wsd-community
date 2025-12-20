package wsd.community.domain.post.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wsd.community.domain.post.request.PostSearchRequest;
import wsd.community.domain.post.response.PostSummaryResponse;
import wsd.community.domain.stats.response.TopWriterResponse;

public interface PostRepositoryCustom {

    Page<PostSummaryResponse> search(PostSearchRequest condition, Pageable pageable);

    Page<PostSummaryResponse> findMyPosts(Long userId, Pageable pageable);

    List<TopWriterResponse> findTopWriters(LocalDateTime startDate, LocalDateTime endDate, int limit);
}
