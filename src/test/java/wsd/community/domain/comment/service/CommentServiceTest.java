package wsd.community.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.comment.entity.Comment;
import wsd.community.domain.comment.entity.CommentLike;
import wsd.community.domain.comment.repository.CommentLikeRepository;
import wsd.community.domain.comment.repository.CommentRepository;
import wsd.community.domain.comment.request.CommentCreateRequest;
import wsd.community.domain.comment.request.CommentUpdateRequest;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("댓글 생성 성공")
    void createComment_Success() {
        // given
        Long userId = 1L;
        Long postId = 1L;
        CommentCreateRequest request = new CommentCreateRequest("content");

        User user = User.builder().email("test@test.com").name("user").build();
        Post post = Post.builder().title("title").content("content").user(user).build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        commentService.createComment(userId, postId, request);

        // then
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_Success() {
        // given
        Long userId = 1L;
        Long commentId = 1L;
        CommentUpdateRequest request = new CommentUpdateRequest("updated content");

        User user = User.builder().email("test@test.com").name("user").build();
        setEntityId(user, userId);

        Comment comment = Comment.builder().content("content").user(user).build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        commentService.updateComment(userId, commentId, request);

        // then
        assertThat(comment.getContent()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("댓글 수정 실패 - 작성자 불일치")
    void updateComment_Fail_Forbidden() {
        // given
        Long userId = 1L;
        Long otherUserId = 2L;
        Long commentId = 1L;
        CommentUpdateRequest request = new CommentUpdateRequest("updated content");

        User owner = User.builder().email("owner@test.com").name("owner").build();
        setEntityId(owner, otherUserId);

        Comment comment = Comment.builder().content("content").user(owner).build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(userId, commentId, request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_COMMENT_WRITER);
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_Success() {
        // given
        Long userId = 1L;
        Long commentId = 1L;

        User user = User.builder().email("test@test.com").name("user").build();
        setEntityId(user, userId);

        Comment comment = Comment.builder().content("content").user(user).build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        commentService.deleteComment(userId, commentId);

        // then
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    @DisplayName("댓글 좋아요 토글 - 생성")
    void toggleLike_Create() {
        // given
        Long userId = 1L;
        Long commentId = 1L;

        User user = User.builder().email("test@test.com").name("user").build();
        Comment comment = Comment.builder().content("content").user(user).build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(commentLikeRepository.findByUserAndComment(user, comment)).willReturn(Optional.empty());

        // when
        commentService.toggleLike(userId, commentId);

        // then
        verify(commentLikeRepository, times(1)).save(any(CommentLike.class));
        assertThat(comment.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 좋아요 토글 - 삭제")
    void toggleLike_Delete() {
        // given
        Long userId = 1L;
        Long commentId = 1L;

        User user = User.builder().email("test@test.com").name("user").build();
        Comment comment = Comment.builder().content("content").user(user).build();
        comment.increaseLikeCount();

        CommentLike commentLike = CommentLike.builder().user(user).comment(comment).build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(commentLikeRepository.findByUserAndComment(user, comment)).willReturn(Optional.of(commentLike));

        // when
        commentService.toggleLike(userId, commentId);

        // then
        verify(commentLikeRepository, times(1)).delete(commentLike);
        assertThat(comment.getLikeCount()).isEqualTo(0);
    }

    private void setEntityId(Object entity, Long id) {
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
