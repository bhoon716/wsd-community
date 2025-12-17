package wsd.community.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wsd.community.domain.post.request.PostSearchCondition;
import wsd.community.domain.post.response.PostSummaryResponse;

public interface PostRepositoryCustom {

    Page<PostSummaryResponse> search(PostSearchCondition condition, Pageable pageable);

    Page<PostSummaryResponse> findMyPosts(Long userId, Pageable pageable);
}
