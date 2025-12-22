package wsd.community.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import wsd.community.common.audit.BaseEntity;
import wsd.community.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE posts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "posts", indexes = {
        @Index(name = "idx_post_title", columnList = "title"),
        @Index(name = "idx_post_created_at", columnList = "created_at"),
        @Index(name = "idx_post_user_id", columnList = "user_id")
})
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostType type;

    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @Column(name = "is_hidden", nullable = false)
    private boolean isHidden;

    @Column(name = "is_pinned", nullable = false)
    private boolean isPinned;

    @Builder
    private Post(String title, String content, User user, PostType type) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.type = type;
        this.likeCount = 0L;
        this.isHidden = false;
        this.isPinned = false;
    }

    public void update(String title, String content, PostType type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount = Math.max(0, this.likeCount - 1);
    }

    public void hide() {
        this.isHidden = true;
    }

    public void togglePin() {
        this.isPinned = !this.isPinned;
    }

    public void translate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
