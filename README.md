# WSD Community

동아리(WSD) 운영을 위한 **커뮤니티 백엔드 API**입니다.  
게시판/댓글/좋아요/게시판 유형, **인기글(Redis 캐싱)**, **JWT 인증/인가(RBAC: GUEST/MEMBER/ADMIN)**, 검색/정렬/페이지네이션, 통계/관리자 기능을 제공합니다.

---

## 주요 기능
- 인증/인가: JWT Access/Refresh, Role 기반 권한 제어(GUEST/MEMBER/ADMIN)
- 소셜 로그인 2종: Google OAuth2 + Firebase Auth(ID Token 검증)
- 게시판 유형(BoardType) 관리(관리자)
- 게시글/댓글 CRUD + 검색/정렬/페이지네이션
- 좋아요(중복 방지) + 카운트
- 인기글(Trending) 조회 + Redis 캐싱(TTL)
- 전역 Rate Limit(예: 비로그인/인증 API 보호)
- 공통 에러 응답 포맷 + 입력 검증
- Health 체크(`/health`)
- Swagger(OpenAPI) 자동 문서
- 자동화 테스트 20+ / 시드 200+ / MySQL(FK, Index) / Flyway 마이그레이션

---

## 기술 스택
- Spring Boot, Spring MVC, Spring Security
- JPA(Hibernate), MySQL, Flyway
- Redis(캐시/레이트리밋/토큰 블랙리스트 등)
- Swagger: springdoc-openapi
- Docker / docker-compose
- Test: JUnit5, Spring Boot Test, Spring Security Test

---

## 배포 주소
- Base URL: `http://<JCLOUD_HOST>:<PORT>`
- Swagger: `http://<JCLOUD_HOST>:<PORT>/swagger-ui`
- OpenAPI JSON: `http://<JCLOUD_HOST>:<PORT>/v3/api-docs`
- Health: `http://<JCLOUD_HOST>:<PORT>/health`

---

## 실행 방법

### 1) Docker(권장)
```bash
# (레포 루트)
cp .env.example .env
docker compose up -d --build
````

### 2) 로컬 실행

```bash
# DB/Redis가 떠 있어야 함(또는 docker로 db/redis만 띄우기)
./gradlew clean test
./gradlew bootRun
```

> Flyway 마이그레이션은 애플리케이션 시작 시 자동 적용됩니다.
> 시드 데이터는 프로젝트 구현에 따라 `seed` 스크립트/명령을 제공합니다. (예: `./gradlew seed` 또는 `java -jar ... --seed=true`)

---

## 환경변수(.env)

`.env`는 **Classroom 제출용**이며 Public Repo에는 올리지 않습니다.

---

## 인증 플로우(요약)

* 일반 로그인: `POST /auth/login` → Access/Refresh 발급
* 갱신: `POST /auth/refresh` → Access 재발급(필요 시 Refresh 회전/블랙리스트)
* Google OAuth2: `GET /auth/google`(로그인 시작) → 콜백 처리 → 우리 JWT 발급
* Firebase: `POST /auth/firebase`(ID Token 전달) → 검증 → 우리 JWT 발급

---

## 역할/권한

* **GUEST**: 공개 조회(정책에 따라 제한), 로그인/회원가입
* **MEMBER**: 게시글/댓글 작성, 좋아요, 내 정보 수정
* **ADMIN**: 사용자/게시판유형/콘텐츠 관리, 통계 조회, 캐시 강제 갱신 등

### 예시 계정

* MEMBER: `user1@wsd.com / P@ssw0rd!`
* ADMIN: `admin@wsd.com / P@ssw0rd!`

---

## API 요약(대표)

* Auth

  * `POST /auth/register`
  * `POST /auth/login`
  * `POST /auth/refresh`
  * `POST /auth/logout`
  * `GET  /auth/google`
  * `POST /auth/firebase`
  * `GET  /auth/me`

* Board Types (Admin 일부)

  * `GET  /board-types`
  * `POST /admin/board-types`
  * `PATCH /admin/board-types/{id}`
  * `DELETE /admin/board-types/{id}`

* Posts / Comments / Likes

  * `GET  /posts` (page/size/sort/keyword/typeId/dateFrom/dateTo)
  * `POST /posts`
  * `GET  /posts/{id}`
  * `PATCH /posts/{id}`
  * `DELETE /posts/{id}`
  * `POST /posts/{id}/like`
  * `DELETE /posts/{id}/like`
  * `POST /posts/{id}/comments`
  * `GET  /posts/{id}/comments`
  * `PATCH /comments/{id}`
  * `DELETE /comments/{id}`

* Trending / Admin / Health

  * `GET  /trending/posts` (Redis 캐시)
  * `POST /admin/cache/trending/refresh`
  * `GET  /admin/stats/daily`
  * `GET  /health` (no-auth)

> 전체 엔드포인트 및 요청/응답 예시는 Swagger에서 확인합니다.

---

## Postman

* 컬렉션: `postman/WSD-Community.postman_collection.json`
* 환경변수 예시:

  * `BASE_URL=http://<JCLOUD_HOST>:<PORT>`
  * `ACCESS_TOKEN=...`
* Pre-request/Test 스크립트 포함(토큰 저장/주입, 상태코드 검증 등)

---

## 테스트

```bash
./gradlew test
```

---

## DB 연결 정보(테스트용)
> 채점 및 로컬 테스트를 위한 DB 접속 정보입니다. (JCloud 내부 또는 포트포워딩 환경)
* **Host**: `<JCLOUD_IP>` 또는 `localhost`
* **Port**: `3306` (MySQL), `6379` (Redis)
* **Database**: `wsd`
* **Username**: `wsd_user`
* **Password**: `********` (Classroom 제출 파일 참조)

---

## 기술 및 보안 고려사항
### 1. 보안(Security)
- **비밀번호 암호화**: BCrypt 해시 알고리즘 적용 (DB 평문 저장 방지)
- **JWT 보안**:
    - Access Token(15분) + Refresh Token(2주) 구조
    - Refresh Token Rotation (RTR) 및 탈취 감지 로직 적용
- **전역 Exception Handling**: 불필요한 스택트레이스 노출 방지 및 일관된 에러 응답

### 2. 성능(Performance)
- **Redis Caching**:
    - 인기글(`Trending`) 조회: TTL 1시간 적용, DB 부하 최소화
    - 반복적인 인증 조회 최소화
- **DB 최적화**:
    - 검색/정렬 필드(`created_at`, `type`, `title`)에 Index 적용
    - N+1 문제 방지를 위한 `BatchSize` 및 `Fetch Join` 적용

---

## 한계점 및 개선 계획
- **CI/CD 파이프라인**: 현재 쉘 스크립트로 배포 중이나, 향후 GitHub Actions 도입 예정
- **대용량 트래픽**: 현재 단일 인스턴스 구조 -> 향후 Docker Swarm/K8s 도입을 통한 Scale-out 고려
- **검색 고도화**: `LIKE` 검색의 한계 -> ElasticSearch 도입 고려


---

## 보안 주의

* `.env`, 키 파일(.pem/.ppk), 비밀번호/시크릿/credentials는 **절대 public repo에 커밋 금지**
* 민감정보는 Classroom에만 제출합니다.
