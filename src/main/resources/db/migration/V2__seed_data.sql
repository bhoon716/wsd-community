-- Users (Password: password1234)
INSERT INTO users (email, password, name, role, created_at, updated_at) VALUES 
('owner@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'OwnerUser', 'OWNER', NOW(), NOW()),
('admin@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'AdminUser', 'ADMIN', NOW(), NOW());

INSERT INTO users (email, password, name, role, created_at, updated_at) VALUES 
('user1@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User1', 'USER', NOW(), NOW()),
('user2@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User2', 'USER', NOW(), NOW()),
('user3@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User3', 'USER', NOW(), NOW()),
('user4@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User4', 'USER', NOW(), NOW()),
('user5@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User5', 'USER', NOW(), NOW()),
('user6@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User6', 'USER', NOW(), NOW()),
('user7@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User7', 'USER', NOW(), NOW()),
('user8@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User8', 'USER', NOW(), NOW()),
('user9@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User9', 'USER', NOW(), NOW()),
('user10@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User10', 'USER', NOW(), NOW()),
('user11@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User11', 'USER', NOW(), NOW()),
('user12@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User12', 'USER', NOW(), NOW()),
('user13@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User13', 'USER', NOW(), NOW()),
('user14@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User14', 'USER', NOW(), NOW()),
('user15@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User15', 'USER', NOW(), NOW()),
('user16@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User16', 'USER', NOW(), NOW()),
('user17@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User17', 'USER', NOW(), NOW()),
('user18@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User18', 'USER', NOW(), NOW()),
('user19@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User19', 'USER', NOW(), NOW()),
('user20@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User20', 'USER', NOW(), NOW());

-- Posts (200 items: ID 1-200)
-- 1-10: Notices (Admin/Owner)
INSERT INTO posts (title, content, user_id, type, like_count, is_hidden, is_pinned, created_at, updated_at) VALUES
('Lorem Ipsum Notice 1', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 1, 'NOTICE', 10, FALSE, TRUE, NOW(), NOW()),
('Lorem Ipsum Notice 2', 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 2, 'NOTICE', 5, FALSE, TRUE, NOW(), NOW()),
('Lorem Ipsum Notice 3', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco.', 1, 'NOTICE', 20, FALSE, FALSE, NOW(), NOW()),
('Lorem Ipsum Notice 4', 'Duis aute irure dolor in reprehenderit in voluptate velit.', 2, 'NOTICE', 15, FALSE, FALSE, NOW(), NOW()),
('Lorem Ipsum Notice 5', 'Excepteur sint occaecat cupidatat non proident.', 1, 'NOTICE', 8, FALSE, FALSE, NOW(), NOW()),
('Lorem Ipsum Notice 6', 'Sunt in culpa qui officia deserunt mollit anim id est laborum.', 2, 'NOTICE', 3, FALSE, FALSE, NOW(), NOW()),
('Lorem Ipsum Notice 7', 'Curabitur pretium tincidunt lacus.', 1, 'NOTICE', 12, FALSE, FALSE, NOW(), NOW()),
('Lorem Ipsum Notice 8', 'Nulla gravida orci a odio.', 2, 'NOTICE', 7, FALSE, FALSE, NOW(), NOW()),
('Lorem Ipsum Notice 9', 'Nullam varius, turpis et commodo pharetra.', 1, 'NOTICE', 4, FALSE, FALSE, NOW(), NOW()),
('Lorem Ipsum Notice 10', 'Est eros bibendum elit, nec luctus magna felis sollicitudin mauris.', 2, 'NOTICE', 6, FALSE, FALSE, NOW(), NOW());

-- 11-200: General/QNA (Users)
INSERT INTO posts (title, content, user_id, type, like_count, is_hidden, is_pinned, created_at, updated_at) VALUES
('Lorem Ipsum Dolor Sit Amet 1', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 3, 'GENERAL', 2, FALSE, FALSE, NOW(), NOW()),
('Consectetur Adipiscing Elit 2', 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 4, 'QNA', 0, FALSE, FALSE, NOW(), NOW()),
('Sed Do Eiusmod Tempor 3', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.', 5, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('Incididunt Ut Labore 4', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum.', 6, 'QNA', 1, FALSE, FALSE, NOW(), NOW()),
('Et Dolore Magna Aliqua 5', 'Excepteur sint occaecat cupidatat non proident, sunt in culpa.', 7, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('Ut Enim Ad Minim 6', 'Curabitur pretium tincidunt lacus. Nulla gravida orci a odio.', 8, 'QNA', 3, FALSE, FALSE, NOW(), NOW()),
('Quis Nostrud Exercitation 7', 'Nullam varius, turpis et commodo pharetra, est eros bibendum elit.', 9, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('Ullamco Laboris Nisi 8', 'Integer in mauris eu nibh euismod gravida.', 10, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('Ut Aliquip Ex Ea 9', 'Duis ac tellus et risus vulputate vehicula.', 11, 'GENERAL', 10, FALSE, FALSE, NOW(), NOW()),
('Commodo Consequat 10', 'Donec lobortis risus a elit. Etiam tempor.', 12, 'QNA', 5, FALSE, FALSE, NOW(), NOW()),
('Duis Aute Irure Dolor 11', 'Ut ullamcorper, ligula eu tempor congue, eros est euismod turpis.', 13, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('In Reprehenderit In 12', 'Maecenas fermentum consequat mi. Donec fermentum.', 14, 'QNA', 0, FALSE, FALSE, NOW(), NOW()),
('Voluptate Velit Esse 13', 'Pellentesque malesuada nulla a mi. Duis sapien sem, aliquet nec.', 15, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('Cillum Dolore Eu 14', 'Aliquam posuere. Vulputate vehicula. Donec lobortis risus.', 16, 'QNA', 7, FALSE, FALSE, NOW(), NOW()),
('Fugiat Nulla Pariatur 15', 'Etiam tempor. Ut ullamcorper, ligula eu tempor congue.', 17, 'GENERAL', 15, FALSE, FALSE, NOW(), NOW()),
('Excepteur Sint Occaecat 16', 'Pellentesque habitant morbi tristique senectus et netus et malesuada.', 18, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('Cupidatat Non Proident 17', 'Fames ac turpis egestas. Proin pharetra nonummy pede.', 19, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('Sunt In Culpa Qui 18', 'Mauris et orci. Aenean nec lorem. In porttitor.', 20, 'QNA', 8, FALSE, FALSE, NOW(), NOW()),
('Officia Deserunt Mollit 19', 'Donec laoreet nonummy augue. Suspendisse dui purus.', 3, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('Anim Id Est Laborum 20', 'Scelerisque at, vulputate vitae, pretium mattis, nunc.', 4, 'QNA', 6, FALSE, FALSE, NOW(), NOW()),
('Curabitur Pretium 21', 'Mauris eget neque at sem venenatis eleifend. Ut nonummy.', 5, 'GENERAL', 20, FALSE, FALSE, NOW(), NOW()),
('Tincidunt Lacus 22', 'Fusce aliquet pede non pede. Suspendisse dapibus lorem.', 6, 'GENERAL', 11, FALSE, FALSE, NOW(), NOW()),
('Nulla Gravida Orci 23', 'Fusce tellus. Sed metus augue, convallis et.', 7, 'QNA', 1, FALSE, FALSE, NOW(), NOW()),
('A Odio Nullam 24', 'Posuere posuere, ante. Fusce enim. Nulla facilisi.', 8, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('Varius Turpis Et 25', 'Pellentesque sed dui ut augue blandit sodales. Vestibulum ante.', 9, 'QNA', 3, FALSE, FALSE, NOW(), NOW()),
('Commodo Pharetra 26', 'Ipsum primis in faucibus orci luctus et ultrices posuere.', 10, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('Est Eros Bibendum 27', 'Cubilia Curae; Aliquam nibh. Mauris ac mauris sed pede.', 11, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW()),
('Elit Nec Luctus 28', 'Pellentesque lobortis. Nulla nisl. Nunc nisl.', 12, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('Magna Felis Sollicitudin 29', 'Duis bibendum, felis sed interdum venenatis, turpis enim.', 13, 'GENERAL', 50, FALSE, FALSE, NOW(), NOW()),
('Mauris Integer In 30', 'Blandit mi, in porttitor pede justo eu massa.', 14, 'QNA', 0, FALSE, FALSE, NOW(), NOW()),
('Mauris Eu Nibh 31', 'Donec dapibus. Duis at velit eu est congue elementum.', 15, 'GENERAL', 2, FALSE, FALSE, NOW(), NOW()),
('Euismod Gravida Duis 32', 'In hac habitasse platea dictumst. Morbi vestibulum.', 16, 'QNA', 1, FALSE, FALSE, NOW(), NOW()),
('Ac Tellus Et 33', 'Volutpat ac, lobortis dignissim, pulvinar sit amet, ante.', 17, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('Risus Vulputate Vehicula 34', 'Vivamus vestibulum nulla nec ante. Sed lacinia.', 18, 'QNA', 0, FALSE, FALSE, NOW(), NOW()),
('Donec Lobortis Risus 35', 'Urna non tincidunt mattis, tortor neque adipiscing diam.', 19, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('A Elit Etiam 36', 'A cursus ipsum ante quis turpis. Nulla facilisi.', 20, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('Tempor Ut Ullamcorper 37', 'Ut fringilla. Suspendisse potenti. Nunc feugiat mi.', 3, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('Ligula Eu Tempor 38', 'A nulla ac enim. In facilisis pharetra mollis.', 4, 'QNA', 4, FALSE, FALSE, NOW(), NOW()),
('Congue Eros Est 39', 'Quisque luctus, quam eget molestie commodo, lacus san.', 5, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('Euismod Turpis Maecenas 40', 'Purus eu velit. Cras nunc nulla, accumsan quis.', 6, 'QNA', 5, FALSE, FALSE, NOW(), NOW()),
('Fermentum Consequat Mi 41', 'Eget, tempus quis, placerat ac, urna.', 7, 'QNA', 10, FALSE, FALSE, NOW(), NOW()),
('Donec Fermentum Pellentesque 42', 'Vestibulum sed, ante. Donec sagittis euismod purus.', 8, 'QNA', 6, FALSE, FALSE, NOW(), NOW()),
('Malesuada Nulla A 43', 'Sed ut perspiciatis unde omnis iste natus error.', 9, 'QNA', 7, FALSE, FALSE, NOW(), NOW()),
('Mi Duis Sapien 44', 'Sit voluptatem accusantium doloremque laudantium.', 10, 'QNA', 3, FALSE, FALSE, NOW(), NOW()),
('Sem Aliquet Nec 45', 'Totam rem aperiam, eaque ipsa quae ab illo inventore.', 11, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('Aliquam Posuere Vulputate 46', 'Veritatis et quasi architecto beatae vitae dicta sunt.', 12, 'QNA', 5, FALSE, FALSE, NOW(), NOW()),
('Vehicula Donec Lobortis 47', 'Explicabo. Nemo enim ipsam voluptatem quia voluptas.', 13, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('Risus Etiam Tempor 48', 'Sit aspernatur aut odit aut fugit, sed quia consequuntur.', 14, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('Ut Ullamcorper Ligula 49', 'Magni dolores eos qui ratione voluptatem sequi nesciunt.', 15, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('Eu Tempor Congue 50', 'Neque porro quisquam est, qui dolorem ipsum quia.', 16, 'GENERAL', 12, FALSE, FALSE, NOW(), NOW()),
('Eros Est Euismod 51', 'Dolor sit amet, consectetur, adipisci velit, sed quia.', 17, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('Turpis Pellentesque Habitant 52', 'Non numquam eius modi tempora incidunt ut labore.', 18, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('Morbi Tristique Senectus 53', 'Et dolore magnam aliquam quaerat voluptatem.', 19, 'GENERAL', 1, FALSE, FALSE, NOW(), NOW()),
('Et Netus Et 54', 'Ut enim ad minima veniam, quis nostrum exercitationem.', 20, 'GENERAL', 2, FALSE, FALSE, NOW(), NOW()),
('Malesuada Fames Ac 55', 'Ullam corporis suscipit laboriosam, nisi ut aliquid.', 3, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('Turpis Egestas Proin 56', 'Ex ea commodi consequatur? Quis autem vel eum iure.', 4, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW()),
('Pharetra Nonummy Pede 57', 'Reprehenderit qui in ea voluptate velit esse quam.', 5, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('Mauris Et Orci 58', 'Nihil molestiae consequatur, vel illum qui dolorem.', 6, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('Aenean Nec Lorem 59', 'Eum fugiat quo voluptas nulla pariatur?', 7, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('In Porttitor Donec 60', 'At vero eos et accusamus et iusto odio dignissimos.', 8, 'GENERAL', 10, FALSE, FALSE, NOW(), NOW()),
('Laoreet Nonummy Augue 61', 'Ducimus qui blanditiis praesentium voluptatum.', 9, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('Suspendisse Dui Purus 62', 'Deleniti atque corrupti quos dolores et quas.', 10, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('Scelerisque At Vulputate 63', 'Molestias excepturi sint occaecati cupiditate non.', 11, 'GENERAL', 15, FALSE, FALSE, NOW(), NOW()),
('Vitae Pretium Mattis 64', 'Provident, similique sunt in culpa qui officia.', 12, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('Nunc Mauris Eget 65', 'Deserunt mollitia animi, id est laborum et dolorum.', 13, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('Neque At Sem 66', 'Fuga. Et harum quidem rerum facilis est et expedita.', 14, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('Venenatis Eleifend Ut 67', 'Distinctio. Nam libero tempore, cum soluta nobis.', 15, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW()),
('Nonummy Fusce Aliquet 68', 'Est eligendi optio cumque nihil impedit quo minus.', 16, 'GENERAL', 2, FALSE, FALSE, NOW(), NOW()),
('Pede Non Pede 69', 'Id quod maxime placeat facere possimus, omnis.', 17, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('Suspendisse Dapibus Lorem 70', 'Voluptas assumenda est, omnis dolor repellendus.', 18, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('Fusce Tellus Sed 71', 'Temporibus autem quibusdam et aut officiis debitis.', 19, 'GENERAL', 11, FALSE, FALSE, NOW(), NOW()),
('Metus Augue Convallis 72', 'Aut rerum necessitatibus saepe eveniet ut et.', 20, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('Et Posuere Posuere 73', 'Voluptates repudiandae sint et molestiae non.', 3, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('Ante Fusce Enim 74', 'Recusandae. Itaque earum rerum hic tenetur a.', 4, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW()),
('Nulla Facilisi Pellentesque 75', 'Sapiente delectus, ut aut reiciendis voluptatibus.', 5, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('Sed Dui Ut 76', 'Maiores alias consequatur aut perferendis doloribus.', 6, 'GENERAL', 12, FALSE, FALSE, NOW(), NOW()),
('Augue Blandit Sodales 77', 'Asperiores repellat.', 7, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('Vestibulum Ante Ipsum 78', 'Lorem ipsum dolor sit amet, consectetur adipiscing.', 8, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('Primis In Faucibus 79', 'Elit, sed do eiusmod tempor incididunt ut labore.', 9, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('Orci Luctus Et 80', 'Et dolore magna aliqua. Ut enim ad minim veniam.', 10, 'GENERAL', 1, FALSE, FALSE, NOW(), NOW()),
('Ultrices Posuere Cubilia 81', 'Quis nostrud exercitation ullamco laboris nisi.', 11, 'GENERAL', 20, FALSE, FALSE, NOW(), NOW()),
('Curae Aliquam Nibh 82', 'Ut aliquip ex ea commodo consequat. Duis aute.', 12, 'GENERAL', 15, FALSE, FALSE, NOW(), NOW()),
('Mauris Ac Mauris 83', 'Irure dolor in reprehenderit in voluptate velit.', 13, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('Sed Pede Pellentesque 84', 'Esse cillum dolore eu fugiat nulla pariatur.', 14, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('Lobortis Nulla Nisl 85', 'Excepteur sint occaecat cupidatat non proident.', 15, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('Nunc Nisl Duis 86', 'Sunt in culpa qui officia deserunt mollit anim.', 16, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('Bibendum Felis Sed 87', 'Id est laborum. Sed ut perspiciatis unde omnis.', 17, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('Interdum Venenatis Turpis 88', 'Iste natus error sit voluptatem accusantium.', 18, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('Enim Blandit Mi 89', 'Doloremque laudantium, totam rem aperiam, eaque.', 19, 'GENERAL', 11, FALSE, FALSE, NOW(), NOW()),
('In Porttitor Pede 90', 'Ipsa quae ab illo inventore veritatis et quasi.', 20, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW());

-- Bulk Insert to reach 200 posts
INSERT INTO posts (title, content, user_id, type, like_count, is_hidden, is_pinned, created_at, updated_at)
SELECT CONCAT(title, ' (Copy)'), content, user_id, type, like_count, is_hidden, is_pinned, NOW(), NOW()
FROM posts WHERE id > 10 AND id <= 120;

-- Comments
INSERT INTO comments (content, post_id, user_id, like_count, is_hidden, created_at, updated_at) VALUES
('Lorem ipsum dolor sit amet.', 1, 3, 2, FALSE, NOW(), NOW()),
('Consectetur adipiscing elit.', 1, 1, 1, FALSE, NOW(), NOW()),
('Sed do eiusmod tempor.', 2, 4, 0, FALSE, NOW(), NOW()),
('Incididunt ut labore.', 3, 5, 3, FALSE, NOW(), NOW()),
('Et dolore magna aliqua.', 3, 6, 1, FALSE, NOW(), NOW()),
('Ut enim ad minim veniam.', 11, 2, 0, FALSE, NOW(), NOW()),
('Quis nostrud exercitation.', 11, 4, 2, FALSE, NOW(), NOW()),
('Ullamco laboris nisi.', 11, 5, 1, FALSE, NOW(), NOW()),
('Ut aliquip ex ea.', 12, 1, 0, FALSE, NOW(), NOW()),
('Commodo consequat.', 13, 6, 1, FALSE, NOW(), NOW());

-- Bulk Insert to reach ~300 comments
INSERT INTO comments (content, post_id, user_id, like_count, is_hidden, created_at, updated_at)
SELECT CONCAT(content, ' (Copy 1)'), post_id, user_id, like_count, is_hidden, NOW(), NOW()
FROM comments WHERE id <= 10;
INSERT INTO comments (content, post_id, user_id, like_count, is_hidden, created_at, updated_at)
SELECT CONCAT(content, ' (Copy 2)'), post_id + 1, user_id, like_count, is_hidden, NOW(), NOW()
FROM comments WHERE id <= 20;
INSERT INTO comments (content, post_id, user_id, like_count, is_hidden, created_at, updated_at)
SELECT CONCAT(content, ' (Copy 3)'), post_id + 2, user_id, like_count, is_hidden, NOW(), NOW()
FROM comments WHERE id <= 40;
INSERT INTO comments (content, post_id, user_id, like_count, is_hidden, created_at, updated_at)
SELECT CONCAT(content, ' (Copy 4)'), post_id + 3, user_id, like_count, is_hidden, NOW(), NOW()
FROM comments WHERE id <= 80;
INSERT INTO comments (content, post_id, user_id, like_count, is_hidden, created_at, updated_at)
SELECT CONCAT(content, ' (Copy 5)'), post_id + 4, user_id, like_count, is_hidden, NOW(), NOW()
FROM comments WHERE id <= 120;

-- Reports
INSERT INTO reports (reporter_id, post_id, reason, description, type, status, action, created_at, updated_at) VALUES 
(3, 11, 'SPAM', 'Lorem ipsum report reason.', 'POST', 'PENDING', 'NO_ACTION', NOW(), NOW()),
(4, 12, 'ABUSE', 'Dolor sit amet report description.', 'POST', 'RESOLVED', 'HIDE', NOW(), NOW()),
(5, 13, 'INAPPROPRIATE', 'Consectetur adipiscing elit report.', 'POST', 'REJECTED', 'NO_ACTION', NOW(), NOW());
