package wsd.community.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.common.error.ErrorCode;
import wsd.community.common.response.CustomException;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.entity.PostLike;
import wsd.community.domain.post.repository.PostLikeRepository;
import wsd.community.domain.post.repository.PostRepository;
import wsd.community.domain.post.request.PostCreateRequest;
import wsd.community.domain.post.request.PostSearchCondition;
import wsd.community.domain.post.request.PostUpdateRequest;
import wsd.community.domain.post.response.PostDetailResponse;
import wsd.community.domain.post.response.PostSummaryResponse;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    public PostDetailResponse getPost(Long postId) {
        Post post = findPostById(postId);
        return PostDetailResponse.from(post);
    }

    public Page<PostSummaryResponse> searchPosts(PostSearchCondition condition, Pageable pageable) {
        return postRepository.search(condition, pageable);
    }

    @Transactional
    public Long createPost(Long userId, PostCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .type(request.type())
                .user(user)
                .build();

        postRepository.save(post);
        return post.getId();
    }

    @Transactional
    public Long updatePost(Long userId, Long postId, PostUpdateRequest request) {
        Post post = findPostById(postId);
        validateWriter(post, userId);

        post.update(request.title(), request.content(), request.type());
        return post.getId();
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = findPostById(postId);
        validateWriter(post, userId);

        postRepository.delete(post);
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
                        },
                        () -> {
                            postLikeRepository.save(new PostLike(user, post));
                            post.increaseLikeCount();
                        });
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateWriter(Post post, Long userId) {
        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
