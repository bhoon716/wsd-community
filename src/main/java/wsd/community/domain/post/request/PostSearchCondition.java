package wsd.community.domain.post.request;

import wsd.community.domain.post.entity.PostType;

public record PostSearchCondition(
        PostType type,
        String keyword) {
}
