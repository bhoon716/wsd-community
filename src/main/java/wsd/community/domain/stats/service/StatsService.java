package wsd.community.domain.stats.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.domain.comment.repository.CommentRepository;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.stats.response.TopCommenterResponse;
import wsd.community.domain.stats.response.TopWriterResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Cacheable(value = "topWriters", key = "#limit + '-' + #days")
    public List<TopWriterResponse> getTopWriters(int limit, int days) {
        log.info("인기 작성자 조회 요청: limit={}, days={}", limit, days);
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<TopWriterResponse> topWriters = postRepository.findTopWriters(startDate, endDate, limit);

        int rank = 0;
        Long prevCount = null;

        for (int i = 0; i < topWriters.size(); i++) {
            TopWriterResponse cur = topWriters.get(i);
            Long curCount = cur.getPostCount();

            if (i == 0 || !Objects.equals(curCount, prevCount)) {
                rank = i + 1;
                prevCount = curCount;
            }
            cur.setRank(rank);
        }

        return topWriters;
    }

    @Cacheable(value = "topCommenters", key = "#limit + '-' + #days")
    public List<TopCommenterResponse> getTopCommenters(int limit, int days) {
        log.info("인기 댓글 작성자 조회 요청: limit={}, days={}", limit, days);
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<TopCommenterResponse> topCommenters = commentRepository.findTopCommenters(startDate, endDate, limit);

        int rank = 0;
        Long prevCount = null;

        for (int i = 0; i < topCommenters.size(); i++) {
            TopCommenterResponse cur = topCommenters.get(i);
            Long curCount = cur.getCommentCount();

            if (i == 0 || !Objects.equals(curCount, prevCount)) {
                rank = i + 1;
                prevCount = curCount;
            }
            cur.setRank(rank);
        }

        return topCommenters;
    }
}
