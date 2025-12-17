package wsd.community.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import wsd.community.common.error.ErrorCode;
import wsd.community.common.response.CustomException;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.entity.PostLike;
import wsd.community.domain.post.entity.PostType;
import wsd.community.domain.post.repository.PostLikeRepository;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.post.request.PostCreateRequest;
import wsd.community.domain.post.request.PostUpdateRequest;
import wsd.community.domain.post.response.PostDetailResponse;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private UserRepository userRepository;

    @Nested
    @DisplayName("게시글 생성")
    class CreatePost {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long userId = 1L;
            PostCreateRequest request = new PostCreateRequest("title", "content", PostType.GENERAL);
            User user = createUser(userId);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            postService.createPost(userId, request);

            // then
            verify(postRepository).save(any(Post.class));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 사용자")
        void fail_userNotFound() {
            // given
            Long userId = 1L;
            PostCreateRequest request = new PostCreateRequest("title", "content", PostType.GENERAL);

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> postService.createPost(userId, request))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_USER);
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class UpdatePost {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long userId = 1L;
            Long postId = 1L;
            PostUpdateRequest request = new PostUpdateRequest("updated title", "updated content", PostType.NOTICE);
            User user = createUser(userId);
            Post post = createPost(postId, user);

            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            Long updatedPostId = postService.updatePost(userId, postId, request);

            // then
            assertThat(updatedPostId).isEqualTo(postId);
            assertThat(post.getTitle()).isEqualTo("updated title");
            assertThat(post.getContent()).isEqualTo("updated content");
            assertThat(post.getType()).isEqualTo(PostType.NOTICE);
        }

        @Test
        @DisplayName("실패: 작성자 불일치")
        void fail_forbidden() {
            // given
            Long userId = 1L;
            Long otherUserId = 2L;
            Long postId = 1L;
            PostUpdateRequest request = new PostUpdateRequest("updated title", "updated content", PostType.NOTICE);
            User user = createUser(userId);
            Post post = createPost(postId, createUser(otherUserId));

            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when & then
            assertThatThrownBy(() -> postService.updatePost(userId, postId, request))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN);
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeletePost {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long userId = 1L;
            Long postId = 1L;
            User user = createUser(userId);
            Post post = createPost(postId, user);

            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            postService.deletePost(userId, postId);

            // then
            verify(postRepository).delete(post);
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회")
    class GetPost {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long postId = 1L;
            User user = createUser(1L);
            Post post = createPost(postId, user);

            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            PostDetailResponse response = postService.getPost(postId);

            // then
            assertThat(response.id()).isEqualTo(postId);
            assertThat(response.title()).isEqualTo(post.getTitle());
            assertThat(response.writerName()).isEqualTo(user.getName());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 게시글")
        void fail_postNotFound() {
            // given
            Long postId = 1L;
            given(postRepository.findById(postId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> postService.getPost(postId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.POST_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("좋아요 토글")
    class ToggleLike {

        @Test
        @DisplayName("성공: 좋아요 생성")
        void success_createLike() {
            // given
            Long userId = 1L;
            Long postId = 1L;
            User user = createUser(userId);
            Post post = createPost(postId, user);

            given(postRepository.findById(postId)).willReturn(Optional.of(post));
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(postLikeRepository.findByUserAndPost(user, post)).willReturn(Optional.empty());

            // when
            postService.toggleLike(userId, postId);

            // then
            verify(postLikeRepository).save(any(PostLike.class));
            assertThat(post.getLikeCount()).isEqualTo(1L);
        }

        @Test
        @DisplayName("성공: 좋아요 취소")
        void success_deleteLike() {
            // given
            Long userId = 1L;
            Long postId = 1L;
            User user = createUser(userId);
            Post post = createPost(postId, user);
            // 좋아요 1개 상태라고 가정
            ReflectionTestUtils.setField(post, "likeCount", 1L);

            PostLike postLike = PostLike.builder().user(user).post(post).build();

            given(postRepository.findById(postId)).willReturn(Optional.of(post));
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(postLikeRepository.findByUserAndPost(user, post)).willReturn(Optional.of(postLike));

            // when
            postService.toggleLike(userId, postId);

            // then
            verify(postLikeRepository).delete(postLike);
            assertThat(post.getLikeCount()).isEqualTo(0L);
        }
    }

    private User createUser(Long id) {
        User user = User.builder()
                .name("test user")
                .email("test@test.com")
                .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private Post createPost(Long id, User user) {
        Post post = Post.builder()
                .title("title")
                .content("content")
                .type(PostType.GENERAL)
                .user(user)
                .build();
        ReflectionTestUtils.setField(post, "id", id);
        return post;
    }
}
