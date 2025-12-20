package wsd.community.domain.stats.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.stats.response.TopWriterResponse;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @InjectMocks
    private StatsService statsService;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("가장 글을 많이 쓴 유저 목록 조회 및 순위 부여 성공")
    void getTopWriters() {
        // Given
        int limit = 5;
        int days = 7;

        TopWriterResponse writer1 = new TopWriterResponse(1L, "User1", "user1@example.com", 10L);
        TopWriterResponse writer2 = new TopWriterResponse(2L, "User2", "user2@example.com", 10L);
        TopWriterResponse writer3 = new TopWriterResponse(3L, "User3", "user3@example.com", 5L);

        List<TopWriterResponse> mockResponse = List.of(writer1, writer2, writer3);

        given(postRepository.findTopWriters(any(LocalDateTime.class), any(LocalDateTime.class), eq(limit)))
                .willReturn(mockResponse);

        // When
        List<TopWriterResponse> result = statsService.getTopWriters(limit, days);

        // Then
        assertThat(result).hasSize(3);

        // Verify Ranking: 1, 1, 3
        assertThat(result.get(0).getRank()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("User1");

        assertThat(result.get(1).getRank()).isEqualTo(1);
        assertThat(result.get(1).getUsername()).isEqualTo("User2");

        assertThat(result.get(2).getRank()).isEqualTo(3);
        assertThat(result.get(2).getUsername()).isEqualTo("User3");

        verify(postRepository).findTopWriters(any(LocalDateTime.class), any(LocalDateTime.class), eq(limit));
    }
}
