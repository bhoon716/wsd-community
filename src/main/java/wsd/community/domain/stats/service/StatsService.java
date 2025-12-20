package wsd.community.domain.stats.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.stats.response.TopWriterResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final PostRepository postRepository;

    public List<TopWriterResponse> getTopWriters(int limit, int days) {
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
}
