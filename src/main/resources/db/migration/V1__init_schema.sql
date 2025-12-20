-- Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    provider VARCHAR(50),
    provider_id VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    deleted_at DATETIME(6),
    INDEX idx_user_name (name)
);

-- Posts Table
CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content LONGTEXT NOT NULL,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    like_count BIGINT NOT NULL DEFAULT 0,
    is_hidden BOOLEAN NOT NULL DEFAULT FALSE,
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    deleted_at DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_post_title (title),
    INDEX idx_post_created_at (created_at),
    INDEX idx_post_user_id (user_id)
);

-- Comments Table
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    like_count BIGINT NOT NULL DEFAULT 0,
    is_hidden BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    deleted_at DATETIME(6),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_comment_post_id (post_id),
    INDEX idx_comment_created_at (created_at)
);

-- Reports Table
CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT NOT NULL,
    post_id BIGINT,
    comment_id BIGINT,
    reason VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,
    resolved_reason VARCHAR(500),
    resolved_by BIGINT,
    resolved_at DATETIME(6),
    canceled_at DATETIME(6),
    target_title VARCHAR(300),
    target_content TEXT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    deleted_at DATETIME(6),
    FOREIGN KEY (reporter_id) REFERENCES users(id),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (comment_id) REFERENCES comments(id),
    FOREIGN KEY (resolved_by) REFERENCES users(id),
    UNIQUE KEY uk_report_reporter_post (reporter_id, post_id),
    UNIQUE KEY uk_report_reporter_comment (reporter_id, comment_id),
    INDEX idx_report_status (status),
    INDEX idx_report_created_at (created_at)
);

-- Post Likes Table
CREATE TABLE post_likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    UNIQUE KEY uk_post_like_user_post (user_id, post_id)
);
