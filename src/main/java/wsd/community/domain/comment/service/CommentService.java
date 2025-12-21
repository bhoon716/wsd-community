package wsd.community.domain.comment.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.common.error.ErrorCode;
import wsd.community.common.error.CustomException;
import wsd.community.domain.comment.entity.Comment;
import wsd.community.domain.comment.entity.CommentLike;
import wsd.community.domain.comment.repository.CommentLikeRepository;
import wsd.community.domain.comment.repository.CommentRepository;
import wsd.community.domain.comment.request.CommentCreateRequest;
import wsd.community.domain.comment.request.CommentUpdateRequest;
import wsd.community.domain.comment.response.CommentResponse;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createComment(Long userId, Long postId, CommentCreateRequest request) {
        log.info("댓글 생성 요청: userId={}, postId={}", userId, postId);
        User user = findUserById(userId);
        Post post = findPostById(postId);

        Comment comment = Comment.builder()
                .content(request.content())
                .post(post)
                .user(user)
                .build();

        commentRepository.save(comment);
        log.info("댓글 생성 완료: commentId={}", comment.getId());
        return comment.getId();
    }

    @Transactional
    public Long updateComment(Long userId, Long commentId, CommentUpdateRequest request) {
        log.info("댓글 수정 요청: userId={}, commentId={}", userId, commentId);
        Comment comment = findCommentById(commentId);
        validateWriter(comment, userId);

        comment.update(request.content());
        log.info("댓글 수정 완료: commentId={}", commentId);
        return comment.getId();
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        log.info("댓글 삭제 요청: userId={}, commentId={}", userId, commentId);
        Comment comment = findCommentById(commentId);
        validateWriter(comment, userId);

        commentRepository.delete(comment);
        log.info("댓글 삭제 완료: commentId={}", commentId);
    }

    public List<CommentResponse> getComments(Long postId) {
        log.info("댓글 목록 조회 요청: postId={}", postId);
        return commentRepository.findAllByPostId(postId);

    }

    @Transactional
    public boolean toggleLike(Long userId, Long commentId) {
        Comment comment = findCommentById(commentId);
        User user = findUserById(userId);

        return commentLikeRepository.findByUserAndComment(user, comment)
                .map(like -> {
                    commentLikeRepository.delete(like);
                    comment.decreaseLikeCount();
                    log.info("댓글 좋아요 취소: userId={}, commentId={}", userId, commentId);
                    return false;
                })
                .orElseGet(() -> {
                    commentLikeRepository.save(new CommentLike(user, comment));
                    comment.increaseLikeCount();
                    log.info("댓글 좋아요 추가: userId={}, commentId={}", userId, commentId);
                    return true;
                });
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateWriter(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_COMMENT_WRITER);
        }
    }
}
