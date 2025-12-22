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
-- Posts (200 items: ID 1-200)
-- 1-10: Notices (Admin/Owner)
INSERT INTO posts (title, content, user_id, type, like_count, is_hidden, is_pinned, created_at, updated_at) VALUES
('동아리 회비 납부 안내', '이번 학기 동아리 회비 납부 기간 및 방법에 대한 안내입니다. 기간 내 납부 부탁드립니다.', 1, 'NOTICE', 10, FALSE, TRUE, NOW(), NOW()),
('신입 회원 환영회 안내', '신입 회원 여러분 환영합니다! 환영회 일정 및 장소 공지합니다. 많은 참석 바랍니다.', 2, 'NOTICE', 5, FALSE, TRUE, NOW(), NOW()),
('정기 총회 일정 공지', '이번 학기 정기 총회 일정이 확정되었습니다. 안건 및 시간 확인 부탁드립니다.', 1, 'NOTICE', 20, FALSE, FALSE, NOW(), NOW()),
('스터디 그룹 모집 안내', '함께 공부할 스터디 그룹을 모집합니다. 관심 있는 분들은 댓글 남겨주세요.', 2, 'NOTICE', 15, FALSE, FALSE, NOW(), NOW()),
('동아리방 이용 수칙', '동아리방 이용 시 지켜야 할 수칙들을 정리했습니다. 깨끗한 이용 부탁드립니다.', 1, 'NOTICE', 8, FALSE, FALSE, NOW(), NOW()),
('여름 MT 장소 확정', '이번 여름 MT 장소가 확정되었습니다. 자세한 내용은 본문을 참고해 주세요.', 2, 'NOTICE', 3, FALSE, FALSE, NOW(), NOW()),
('운영진 회의 결과 보고', '지난주 진행된 운영진 회의 결과 공유드립니다.', 1, 'NOTICE', 12, FALSE, FALSE, NOW(), NOW()),
('비품 구매 요청 안내', '동아리 비품 구매가 필요한 경우 해당 게시판을 통해 신청해 주세요.', 2, 'NOTICE', 7, FALSE, FALSE, NOW(), NOW()),
('서버 점검 예정 안내', '이번 주말 서버 점검이 예정되어 있습니다. 이용에 불편을 드려 죄송합니다.', 1, 'NOTICE', 4, FALSE, FALSE, NOW(), NOW()),
('해커톤 참가자 모집', '교내 해커톤에 참가할 팀원을 모집합니다. 개발자, 디자이너 환영합니다.', 2, 'NOTICE', 6, FALSE, FALSE, NOW(), NOW());

-- 11-200: General/QNA (Users)
INSERT INTO posts (title, content, user_id, type, like_count, is_hidden, is_pinned, created_at, updated_at) VALUES
('안녕하세요 가입인사 드립니다!', '반갑습니다! 열심히 활동하겠습니다.', 3, 'GENERAL', 2, FALSE, FALSE, NOW(), NOW()),
('스프링 부트 질문있습니다.', 'JPA 사용 중에 N+1 문제가 발생하는데 해결 방법이 궁금합니다.', 4, 'QNA', 0, FALSE, FALSE, NOW(), NOW()),
('오늘 점심 메뉴 추천 좀요', '학식 vs 나가서 먹기 고민되네요. 추천 받습니다.', 5, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('AWS 배포 관련해서 질문드립니다.', 'EC2 인스턴스 접속이 안 되는데 보안 그룹 문제일까요?', 6, 'QNA', 1, FALSE, FALSE, NOW(), NOW()),
('이번 시험 공부 다들 잘 되시나요?', '중간고사 범위가 너무 많아서 힘드네요 ㅠㅠ', 7, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('맥북 vs 윈도우 노트북 추천해주세요', '개발 입문하려는데 어떤 노트북이 좋을까요?', 8, 'QNA', 3, FALSE, FALSE, NOW(), NOW()),
('주말에 같이 코딩하실 분?', '카페에서 각자 코딩하실 분 구합니다.', 9, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('리액트 상태 관리 라이브러리 뭐 쓰시나요?', 'Redux, Recoil, Zustand 중에서 고민입니다.', 10, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('요즘 뜨는 기술 스택이 뭔가요?', '취업 준비 중인데 트렌드가 궁금합니다.', 11, 'GENERAL', 10, FALSE, FALSE, NOW(), NOW()),
('깃허브 커밋 잔디 심기 힘드네요', '1일 1커밋 도전 중인데 쉽지 않습니다.', 12, 'QNA', 5, FALSE, FALSE, NOW(), NOW()),
('개발자 로드맵 질문', '백엔드 개발자가 되려면 뭐부터 공부해야 할까요?', 13, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('알고리즘 문제 추천해주세요', '백준이나 프로그래머스 풀고 있는데 좋은 문제 없을까요?', 14, 'QNA', 0, FALSE, FALSE, NOW(), NOW()),
('첫 프로젝트 후기', '우여곡절 끝에 첫 프로젝트를 완성했습니다. 뿌듯하네요.', 15, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('면접 질문 리스트 공유합니다', '제가 최근에 받은 기술 면접 질문들 정리해봤습니다.', 16, 'QNA', 7, FALSE, FALSE, NOW(), NOW()),
('도커 컨테이너가 자꾸 죽어요', '로그를 봐도 원인을 모르겠는데 도와주세요 ㅠㅠ', 17, 'GENERAL', 15, FALSE, FALSE, NOW(), NOW()),
('사이드 프로젝트 팀원 구합니다', '웹 서비스 런칭을 목표로 하실 분들 찾습니다.', 18, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('개발 공부 번아웃 왔을 때 팁', '다들 슬럼프 어떻게 극복하시나요?', 19, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('인프런 강의 추천 부탁드립니다', '스프링 부트 입문용 강의 찾고 있습니다.', 20, 'QNA', 8, FALSE, FALSE, NOW(), NOW()),
('오늘 날씨 너무 좋네요', '코딩만 하기에는 아까운 날씨입니다.', 3, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('깃 충돌 해결하는 법', '머지하다가 충돌 났는데 어떻게 해야 하나요?', 4, 'QNA', 6, FALSE, FALSE, NOW(), NOW()),
('개발 유튜브 채널 추천', '유익한 개발 관련 유튜버 추천해 주세요.', 5, 'GENERAL', 20, FALSE, FALSE, NOW(), NOW()),
('REST API 설계 질문', 'URI 네이밍 규칙이 헷갈립니다.', 6, 'GENERAL', 11, FALSE, FALSE, NOW(), NOW()),
('자바 vs 파이썬', '첫 언어로 어떤 걸 추천하시나요?', 7, 'QNA', 1, FALSE, FALSE, NOW(), NOW()),
('클린 코드 읽어보신 분?', '책 내용이 어떤지 궁금합니다.', 8, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('mysql 인덱스 질문', '인덱스를 걸었는데도 쿼리가 느립니다.', 9, 'QNA', 3, FALSE, FALSE, NOW(), NOW()),
('개발자 노트북 스티커', '다들 노트북에 스티커 많이 붙이시나요?', 10, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('코딩용 키보드 추천', '기계식 키보드 입문하려고 합니다.', 11, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW()),
('oauth2 구현 질문', '구글 로그인 연동 중에 에러가 납니다.', 12, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('취업 포트폴리오 조언', '포트폴리오 구성 팁 좀 주세요.', 13, 'GENERAL', 50, FALSE, FALSE, NOW(), NOW()),
('테스트 코드 작성 팁', 'Junit5 사용법이 익숙하지 않네요.', 14, 'QNA', 0, FALSE, FALSE, NOW(), NOW()),
('배포 자동화 구축 후기', 'Github Actions로 CI/CD 구축했습니다. 너무 편하네요.', 15, 'GENERAL', 2, FALSE, FALSE, NOW(), NOW()),
('서버 비용 얼마나 나오시나요?', 'AWS 프리티어 끝나면 비용이 걱정됩니다.', 16, 'QNA', 1, FALSE, FALSE, NOW(), NOW()),
('개발 동아리 활동 질문', '동아리 활동이 취업에 도움이 많이 될까요?', 17, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('자소서 작성 중인데 힘들네요', '항목 채우기가 너무 어렵습니다.', 18, 'QNA', 0, FALSE, FALSE, NOW(), NOW()),
('정보처리기사 자격증', '따는 게 좋을까요?', 19, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('코딩 테스트 준비 기간', '보통 얼마나 준비하시나요?', 20, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('개발자 필독서 리스트', '추천받은 책들 정리했습니다.', 3, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('SQL 쿼리 최적화', 'JOIN이 많아지니 성능이 떨어집니다.', 4, 'QNA', 4, FALSE, FALSE, NOW(), NOW()),
('프론트엔드 프레임워크', 'Vue vs React vs Angular', 5, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('모바일 앱 개발', 'Flutter와 React Native 중 고민입니다.', 6, 'QNA', 5, FALSE, FALSE, NOW(), NOW()),
('서버리스 아키텍처', 'Lambda 서비스 사용해보신 분 계신가요?', 7, 'QNA', 10, FALSE, FALSE, NOW(), NOW()),
(' MSA vs Monolithic', '어떤 아키텍처를 선호하시나요?', 8, 'QNA', 6, FALSE, FALSE, NOW(), NOW()),
('개발자 커뮤니티 추천', '활동하기 좋은 커뮤니티 있나요?', 9, 'QNA', 7, FALSE, FALSE, NOW(), NOW()),
('IT 뉴스레터 구독', '요즘 읽을만한 뉴스레터 추천 좀 해주세요.', 10, 'QNA', 3, FALSE, FALSE, NOW(), NOW()),
('CS 지식 공부법', '따로 스터디하시나요?', 11, 'QNA', 2, FALSE, FALSE, NOW(), NOW()),
('개발자의 건강 관리', '거북목이랑 손목 통증 관리는 어떻게 하시나요?', 12, 'QNA', 5, FALSE, FALSE, NOW(), NOW()),
('재택 근무 장단점', '재택 하시는 분들 만족하시나요?', 13, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('연봉 협상 팁', '첫 이직인데 팁 좀 부탁드립니다.', 14, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('개발자 밈(Meme) 모음', '웃긴 짤들 공유해요.', 15, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('코드 리뷰 문화', '좋은 코드 리뷰란 무엇일까요?', 16, 'GENERAL', 12, FALSE, FALSE, NOW(), NOW()),
('기술 부채 해결', '리팩토링은 언제 하시나요?', 17, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('오픈 소스 기여', '처음 시작하려면 어떻게 해야 할까요?', 18, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('개발 컨퍼런스 후기', '이번에 다녀온 컨퍼런스 후기 남깁니다.', 19, 'GENERAL', 1, FALSE, FALSE, NOW(), NOW()),
('디자인 패턴 스터디', '디자인 패턴 같이 공부하실 분!', 20, 'GENERAL', 2, FALSE, FALSE, NOW(), NOW()),
('타입스크립트 도입 후기', '확실히 버그가 줄어드는 느낌입니다.', 3, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('그래픽 카드 추천', '딥러닝 공부용으로 사려고 합니다.', 4, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW()),
('게임 개발 관심있으신 분', 'Unity나 Unreal 엔진 공부하시는 분 계신가요?', 5, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('블록체인 기술 전망', 'web3 쪽은 어떻게 생각하시나요?', 6, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('보안 기사 자격증', '난이도가 어떤가요?', 7, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('클라우드 자격증', 'AWS SAA 준비 중입니다.', 8, 'GENERAL', 10, FALSE, FALSE, NOW(), NOW()),
('데이터 분석가 진로', '필요한 역량이 무엇일까요?', 9, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('인공지능 대학원', '진학 고민 중입니다.', 10, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('스타트업 vs 대기업', '첫 직장으로 어디가 좋을까요?', 11, 'GENERAL', 15, FALSE, FALSE, NOW(), NOW()),
('해외 취업 준비', '영어 공부는 어떻게 하시나요?', 12, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('개발자의 취미 생활', '코딩 말고 다른 취미 있으신가요?', 13, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('야근 많이 하시나요?', '워라밸이 궁금합니다.', 14, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('개발자 노트북 가방', '수납 공간 넉넉한 백팩 추천해주세요.', 15, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW()),
('스탠딩 책상 후기', '서서 일하니까 허리가 덜 아픈 것 같아요.', 16, 'GENERAL', 2, FALSE, FALSE, NOW(), NOW()),
('모니터암 추천', '책상이 좁아서 모니터암을 사려고 합니다.', 17, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('기계식 키보드 축 추천', '청축, 적축, 갈축 중에 뭐가 좋을까요?', 18, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('무선 마우스 추천', '로지텍 마우스가 좋나요?', 19, 'GENERAL', 11, FALSE, FALSE, NOW(), NOW()),
('데스크탑 견적 문의', '개발용 PC 견적 좀 봐주세요.', 20, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('에러 로그 분석', '도저히 원인을 모르겠어요.', 3, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('주니어 개발자의 고민', '성장하고 있는지 잘 모르겠습니다.', 4, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW()),
('시니어 개발자가 되려면', '어 어떤 역량을 키워야 할까요?', 5, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('팀장님의 칭찬', '오늘 코드 리뷰에서 칭찬 받았습니다 ㅎㅎ', 6, 'GENERAL', 12, FALSE, FALSE, NOW(), NOW()),
('퇴근 10분 전', '집에 가고 싶네요.', 7, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('월요병 극복', '다들 화이팅입니다!', 8, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('불금엔 치킨', '오늘 저녁은 치킨입니다.', 9, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('주말 코딩', '주말에도 코딩하는 저, 정상인가요?', 10, 'GENERAL', 1, FALSE, FALSE, NOW(), NOW()),
('커피 수혈 시급', '카페인 없이는 코딩이 안 됩니다.', 11, 'GENERAL', 20, FALSE, FALSE, NOW(), NOW()),
('에너지 드링크 추천', '몬스터 vs 핫식스', 12, 'GENERAL', 15, FALSE, FALSE, NOW(), NOW()),
('야식 메뉴 추천', '배가 고프네요.', 13, 'GENERAL', 8, FALSE, FALSE, NOW(), NOW()),
('운동 추천', '체력이 떨어져서 운동을 시작하려 합니다.', 14, 'GENERAL', 4, FALSE, FALSE, NOW(), NOW()),
('다이어트 식단', '개발하면서 살 빼기 힘드네요.', 15, 'GENERAL', 6, FALSE, FALSE, NOW(), NOW()),
('영양제 챙겨 드시나요?', '눈 건강에 좋은 영양제 추천 좀요.', 16, 'GENERAL', 3, FALSE, FALSE, NOW(), NOW()),
('개발자의 패션', '체크 남방 국룰인가요?', 17, 'GENERAL', 5, FALSE, FALSE, NOW(), NOW()),
('후드티 브랜드 추천', '편하게 입을 후드티 찾습니다.', 18, 'GENERAL', 9, FALSE, FALSE, NOW(), NOW()),
('슬리퍼 추천', '사무실에서 신을 편한 슬리퍼.', 19, 'GENERAL', 11, FALSE, FALSE, NOW(), NOW()),
('텀블러 추천', '보온 잘 되는 텀블러 있을까요?', 20, 'GENERAL', 7, FALSE, FALSE, NOW(), NOW());

-- Bulk Insert to reach 200 posts
INSERT INTO posts (title, content, user_id, type, like_count, is_hidden, is_pinned, created_at, updated_at)
SELECT CONCAT(title, ' (복사본)'), content, user_id, type, like_count, is_hidden, is_pinned, NOW(), NOW()
FROM posts WHERE id > 10 AND id <= 120;

-- Comments
INSERT INTO comments (content, post_id, user_id, like_count, is_hidden, created_at, updated_at) VALUES
('환영합니다!', 1, 3, 2, FALSE, NOW(), NOW()),
('좋은 정보 감사합니다.', 1, 1, 1, FALSE, NOW(), NOW()),
('저도 궁금하네요.', 2, 4, 0, FALSE, NOW(), NOW()),
('참여하고 싶습니다.', 3, 5, 3, FALSE, NOW(), NOW()),
('화이팅입니다!', 3, 6, 1, FALSE, NOW(), NOW()),
('저요!', 11, 2, 0, FALSE, NOW(), NOW()),
('도움이 많이 되었습니다.', 11, 4, 2, FALSE, NOW(), NOW()),
('감사합니다.', 11, 5, 1, FALSE, NOW(), NOW()),
('축하드려요!', 12, 1, 0, FALSE, NOW(), NOW()),
('고생하셨습니다.', 13, 6, 1, FALSE, NOW(), NOW());

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
