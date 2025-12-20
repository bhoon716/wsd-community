package wsd.community.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.entity.PostLike;
import wsd.community.domain.post.repository.PostLikeRepository;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.post.request.PostCreateRequest;
import wsd.community.domain.post.request.PostUpdateRequest;
import wsd.community.domain.post.response.PostDetailResponse;
import wsd.community.domain.post.response.PostSummaryResponse;
import java.util.List;
import wsd.community.domain.comment.repository.CommentRepository;
import wsd.community.domain.comment.response.CommentResponse;
import wsd.community.domain.post.entity.PostType;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.domain.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public PostDetailResponse getPost(Long postId) {
        log.info("게시글 조회 요청: postId={}", postId);
        Post post = findPostById(postId);
        List<CommentResponse> comments = commentRepository.findAllByPostId(postId);

        return PostDetailResponse.from(post, comments);
    }

    public Page<PostSummaryResponse> searchPosts(PostType type, String keyword, Pageable pageable) {
        log.info("게시글 검색 요청: type={}, keyword={}", type, keyword);
        return postRepository.search(type, keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getMyPosts(Long userId, Pageable pageable) {
        log.info("내 게시글 조회 요청: userId={}", userId);
        return postRepository.findMyPosts(userId, pageable);
    }

    @Transactional
    public Long createPost(Long userId, PostCreateRequest request) {
        log.info("게시글 생성 요청: userId={}, type={}", userId, request.type());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        validateAdminRole(user, request.type());

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .type(request.type())
                .user(user)
                .build();

        postRepository.save(post);
        log.info("게시글 생성 완료: postId={}", post.getId());
        return post.getId();
    }

    @Transactional
    public Long updatePost(Long userId, Long postId, PostUpdateRequest request) {
        log.info("게시글 수정 요청: userId={}, postId={}", userId, postId);
        Post post = findPostById(postId);

        validateWriter(post, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        validateAdminRole(user, request.type());

        post.update(request.title(), request.content(), request.type());
        log.info("게시글 수정 완료: postId={}", postId);
        return post.getId();
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        log.info("게시글 삭제 요청: userId={}, postId={}", userId, postId);
        Post post = findPostById(postId);
        validateWriter(post, userId);

        postRepository.delete(post);
        log.info("게시글 삭제 완료: postId={}", postId);
    }

    @Transactional
    public void toggleLike(Long userId, Long postId) {
        Post post = findPostById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        postLikeRepository.findByUserAndPost(user, post)
                .ifPresentOrElse(
                        postLike -> {
                            postLikeRepository.delete(postLike);
                            post.decreaseLikeCount();
                            log.info("게시글 좋아요 취소: userId={}, postId={}", userId, postId);
                        },
                        () -> {
                            postLikeRepository.save(new PostLike(user, post));
                            post.increaseLikeCount();
                            log.info("게시글 좋아요 추가: userId={}, postId={}", userId, postId);
                        });
    }

    @Transactional
    public void togglePin(Long userId, Long postId) {
        log.info("게시글 고정 토글 요청: userId={}, postId={}", userId, postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.OWNER) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }

        Post post = findPostById(postId);
        post.togglePin();
        log.info("게시글 고정 토글 완료: postId={}, isPinned={}", postId, post.isPinned());
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateWriter(Post post, Long userId) {
        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_POST_WRITER);
        }
    }

    private void validateAdminRole(User user, PostType postType) {
        if (postType == PostType.NOTICE && user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.OWNER) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }
    }
}
