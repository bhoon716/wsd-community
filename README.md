# WSD Community Project

> **2025-2학기 Web System Design Term Project**  
> 고성능, 고가용성을 지향하는 동아리 커뮤니티 백엔드 서비스

---

## 1. 프로젝트 개요
**WSD Community**는 대학 동아리 운영을 위한 커뮤니티 플랫폼의 백엔드 시스템입니다.  

### 필수 구현 기능
-   **게시판**: 일반/질문 게시글 작성, 조회(검색/정렬/페이징), 수정, 삭제.
-   **댓글**: 게시글에 대한 댓글 작성 및 조회, 대댓글(선택적).
-   **신고 시스템**: 부적절한 게시글/댓글 신고 및 관리자 처리 프로세스.
-   **통계**: 활동량이 많은 우수 작성자/답변자 랭킹 집계.
-   **인증/인가**: JWT 기반 자체 로그인 및 Firebase 소셜 로그인, RBAC(User/Admin/Owner) 권한 관리.

### 추가 구현 기능
-   **게시글 번역**: Google Cloud Translation API를 이용한 AI 자동 번역.
-   **시스템 모니터링**: Prometheus + Grafana를 통한 서버 상태 시각화.
-   **CI/CD**: GitHub Actions를 통한 자동 빌드 및 배포 파이프라인 구축.

### 사용 기술
| 구분 | 기술 / 버전 | 비고 |
| :--- | :--- | :--- |
| **Framework** | Spring Boot 3.5.8 | Java 21 | |
| **Database** | MySQL 8.0 | Main DB |
| **Cache** | Redis (Alpine) | Session, Rate Limiting |
| **Security** | Spring Security, JWT | Authentication / Authorization |
| **Infra** | Docker, Docker Compose | Containerization |
| **CI/CD** | GitHub Actions | Automated Build & Deploy |
| **Monitoring** | Prometheus, Grafana | System Metrics Visualization |
| **External** | Firebase Auth/ML, Google OAuth2 | Social Login, AI Translation |

### 프로젝트 구조
```text
wsd-community-backend/
├── .github/workflows/    # CI/CD Pipeline
├── docs/                 # Documentation
├── prometheus/           # Monitoring Config
├── src/main/java/        # Source Code
├── src/main/resources/   # App Config
├── docker-compose.yml    # Container Orchestration
└── README.md             # Project Guide
```

### 상세 문서
-   [**시스템 아키텍처**](docs/architecture.md)
-   [**API 상세 설계**](docs/api-design.md)
-   [**DB 스키마 설계**](docs/db-schema.md)

---

## 2. 실행 및 접속 가이드

### 1) 로컬 실행 (Docker Compose)
데이터베이스와 애플리케이션을 통합 실행하는 권장 방법입니다.

```bash
# 1. 환경변수 설정
cp .env.example .env

# 2. 실행
docker compose up -d --build
```
-   **서버 접속**: `http://localhost:80`
-   **Swagger UI**: `http://localhost:80/swagger-ui/index.html`

### 2) 테스트 실행 (Testing)
단위 테스트 및 통합 테스트를 수행하여 시스템 무결성을 검증합니다.
```bash
./gradlew clean test
```

### 2) JCloud 배포 환경 접속
실제 운영 중인 서버 정보입니다.

-   **API Server**: [http://113.198.66.75:18119](http://113.198.66.75:18119/health)
-   **Swagger UI**: [http://113.198.66.75:18119/swagger-ui/index.html](http://113.198.66.75:18119/swagger-ui/index.html)
-   **Grafana**: [http://113.198.66.75:10119](http://113.198.66.75:10119/d/spring_boot_21/spring-boot-2-1-system-monitor?orgId=1&from=now-1h&to=now&timezone=browser&var-application=&var-instance=app:8080&var-hikaricp=HikariPool-1&var-memory_pool_heap=$__all&var-memory_pool_nonheap=$__all&refresh=5s) (계정: `.env` 참조)

> **원격 DB/Redis 접속 (SSH)**
> ```bash
> # 1. SSH 접속 (Port: 19119)
> ssh -p 19119 ubuntu@113.198.66.75
> 
> # 2. MySQL 접속
> docker exec -it wsd_community_db mysql -u user -p
> 
> # 3. Redis 접속
> docker exec -it wsd_community_redis redis-cli -a redis_password
> ```

### 3) 로그인 가이드
본 프로젝트는 **Google OAuth2**와 **Firebase Auth** 두 가지 방식을 지원합니다.

#### A. Google OAuth2 (백엔드 통합)
Spring Security OAuth2 Client를 이용한 방식입니다.
-   **접속 URL**: `http://localhost:80/oauth2/authorization/google`
-   **동작**: 접속 시 구글 로그인 창으로 리다이렉트되며, 로그인 성공 시 백엔드에서 자체 JWT(Access/Refresh)를 쿠키/헤더로 발급합니다.

#### B. Firebase Auth (클라이언트 연동)
프론트엔드에서 Firebase SDK로 로그인 후, 발급받은 `ID Token`을 서버로 전송하는 방식입니다.
-   **테스트 파일**: `docs/firebase-login-test.html`
-   **사용법**:
    1.  위 파일을 브라우저로 엽니다.
    2.  본인의 Firebase Config(ApiKey 등)를 입력하고 '초기화'를 누릅니다.
    3.  '구글 로그인' 또는 '이메일 로그인'을 진행합니다.
    4.  로그인 성공 시 '백엔드로 토큰 전송' 버튼을 눌러 실제 서버 로그인을 검증합니다.

### 3) 예제 계정
| Role | Email | Password | 주요 권한 |
| --- | --- | --- | --- |
| **USER** | `user1@test.com` | `password1234` | 게시글 작성, 댓글, 신고 |
| **ADMIN** | `admin@test.com` | `password1234` | 게시글 관리, 댓글 관리, 신고 처리 |
| **OWNER** | `owner@test.com` | `password1234` | 모든 권한 |

---

## 3. 환경설정

### 주요 환경변수 (.env)
`.env.example`을 참고하여 설정합니다.
| 변수명 | 설명 |
| :--- | :--- |
| `DB_USER` | DB 사용자명 |
| `DB_PASSWORD` | DB 비밀번호 |
| `DB_ROOT_PASSWORD` | DB 루트 비밀번호 |
| `JWT_SECRET` | JWT 서명 키 (32자 이상) |
| `FIREBASE_SERVICE_ACCOUNT` | Firebase Admin SDK 설정 (JSON) |
| `GRAFANA_ADMIN_USER` | Grafana 초기 ID |
| `GRAFANA_ADMIN_PASSWORD` | Grafana 초기 PW |

### Postman 설정
`postman/wsd_community_collection.json`을 Import하여 사용하세요.
-   **baseUrl**: `http://113.198.66.75:18119` (JCloud) 또는 `http://localhost:80` (Local)
-   **firebase_api_key**: Firebase Web API Key (별도 설정 필요. 클래스룸 별도 제출)

---

## 4. 시스템 설계 및 정책

### 인증 및 권한
-   **로그인**: Firebase(소셜) 또는 자체 로그인 -> JWT(Access/Refresh) 발급.
-   **Role**:
    -   `USER`: 일반 사용자 (글작성, 신고)
    -   `ADMIN`: 관리자 (글관리, 신고처리)
    -   `OWNER`: 최고 관리자 (시스템 설정)

### 역할/권한표 (Role & Permissions)
| API 그룹 | Method | `USER` | `ADMIN` | `OWNER` | 비고 |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **게시글** | GET (목록/상세) | ✅ | ✅ | ✅ | 비로그인 가능 |
| **게시글** | POST (작성) | ✅ | ✅ | ✅ | |
| **게시글** | PUT/DELETE | ✅ (본인) | ✅ | ✅ | 관리자는 모든 글 관리 가능 |
| **댓글** | POST/PUT/DELETE | ✅ (본인) | ✅ | ✅ | |
| **신고** | POST (생성) | ✅ | ✅ | ✅ | |
| **신고** | GET/PUT (처리) | ❌ | ✅ | ✅ | 관리자 전용 |
| **사용자** | PUT (Role변경) | ❌ | ❌ | ✅ | 오너 전용 |

### 인증 플로우 설명 (Auth Flow)
1. **로그인 요청**: Client가 `Google OAuth2` 또는 `Firebase Auth`를 통해 인증을 시도합니다.
2. **토큰 발급**: 인증 성공 시, Server는 `Access Token` (1시간)과 `Refresh Token` (7일)을 발급합니다.
3. **API 접근**: Client는 `Authorization: Bearer <Access Token>` 헤더를 포함하여 API를 호출합니다.
4. **권한 검증**: Server는 `JWT Filter`에서 토큰 유효성과 사용자 `Role`을 검증하여 접근을 허용/거부합니다.
5. **토큰 갱신**: Access Token 만료 시, `Refresh Token`을 사용하여 새로운 토큰을 발급받습니다 (`POST /auth/reissue`).

### 성능 및 보안
-   **Rate Limiting**: IP당 요청 횟수 제한 (Bucket4j).
-   **Security**: 모든 비밀번호 BCrypt 해싱, CORS 정책 적용.
-   **Optimization**: Fetch Join(N+1 방지), 주요 컬럼 Indexing.

### 에러 처리
일관된 JSON 응답 포맷을 사용하며, 주요 에러 코드는 다음과 같습니다.
-   `U00x`: 사용자 관련 (없음, 중복 등)
-   `A00x`: 인증/권한 관련 (토큰 만료, 접근 거부)
-   `P00x`: 게시글 관련
-   `R00x`: 신고 시스템 관련

---

## 5. 과제 필수 구현 항목

### 5.1 인증 및 인가
- **JWT 기반 인증**: Access Token(1h) + Refresh Token(7d, Redis) 전략을 사용하여 보안과 편의성을 확보했습니다.
- **소셜 로그인 (2종)**: Firebase Auth와 Google OAuth2 두 가지 방식을 모두 구현했습니다.
- **RBAC**: `USER`, `ADMIN`, `OWNER` 3단계 권한 체계를 구축하고, Custom Annotation으로 엄격하게 권한을 제어합니다.

### 5.2 데이터베이스 및 성능
- **MySQL & JPA**: JPA와 QueryDSL을 활용하여 N+1 문제를 방지하고, 복잡한 동적 쿼리와 페이징을 효율적으로 처리했습니다.
- **Redis 활용**: Refresh Token 저장소 및 Bucket4j 기반 Rate Limiting(API 요청 제한)에 사용됩니다.
- **마이그레이션**: Flyway를 통해 DB 스키마(`V1`)와 시드 데이터(`V2`)를 버전 관리합니다.

### 5.3 API 및 예외 처리
- **RESTful API**: 총 30개 이상의 엔드포인트를 구현했으며, 검색/정렬/필터링을 지원합니다. (상세: [API 문서](docs/api-design.md))

### 5.3 API 엔드포인트 요약 (API Summary)
| Method | URI | 설명 | 권한 |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/firebase` | Firebase 로그인 (ID Token) | ALL |
| `GET` | `/api/posts` | 게시글 목록 조회 (검색/정렬) | ALL |
| `POST` | `/api/posts` | 게시글 작성 | USER+ |
| `POST` | `/api/reports` | 게시글/댓글 신고 | USER+ |
| `GET` | `/api/admin/reports` | 신고 목록 조회 | ADMIN+ |
| `PUT` | `/api/users/{id}/role` | 사용자 권한 변경 | OWNER |
| `GET` | `/health` | 서버 헬스 체크 | ALL |
*(전체 목록은 상단 API 문서 링크 참조)*
- **입력 검증**: `@Valid`와 DTO를 통해 요청 데이터의 타입과 범위를 검증하며, 유효성 실패 시 상세 사유를 반환합니다.
- **에러 응답 통일**: `GlobalExceptionHandler`를 통해 모든 예외를 일관된 JSON 포맷(`code`, `message`, `details`)으로 반환합니다.
- **Http Status**: 성공(2xx), 클라이언트 오류(4xx), 서버 오류(5xx) 등 상황에 맞는 상태 코드를 적절히 사용합니다.

### 5.4 로깅 및 테스트
- **로깅 시스템**: AOP를 적용하여 API 요청/응답 요약 정보(Method, Path, Status, Latency)와 에러 스택 트레이스를 기록합니다.
- **Health Check**: `/health` 엔드포인트를 통해 서버의 가용성을 확인합니다.
- **테스트 코드**: JUnit5와 Mockito를 활용하여 컨트롤러 및 서비스 레이어에 대해 60개 이상의 테스트를 작성했습니다.
- **API 문서화**: Swagger 및 Postman Collection을 통해 API 명세와 테스트 환경을 제공합니다.

### 5.5 인프라 및 배포
- **Docker**: Multi-stage build를 적용한 최적화된 `Dockerfile`과 서비스 통합 관리를 위한 `docker-compose.yml`을 작성했습니다.
- **JCloud 배포**: 실제 운영 환경에 배포되어 있으며, 헬스 체크(`GET /health`)를 통해 가용성을 보장합니다.

### 5.6 문서화
- **Swagger/OpenAPI**: API 명세서를 자동화하여 `/swagger-ui/index.html`에서 제공합니다.
- **Postman**: 환경 변수와 테스트 스크립트가 포함된 컬렉션을 제공하여 API 테스트 편의성을 높였습니다.

---

## 6. 추가 구현 기능

### 6.1 시스템 모니터링
Prometheus와 Grafana를 활용하여 서버의 주요 지표를 실시간으로 수집하고 시각화합니다.
-   **대시보드 접속**: [http://113.198.66.75:10119](http://113.198.66.75:10119/d/spring_boot_21/spring-boot-2-1-system-monitor?orgId=1&from=now-1h&to=now&timezone=browser&var-application=&var-instance=app:8080&var-hikaricp=HikariPool-1&var-memory_pool_heap=$__all&var-memory_pool_nonheap=$__all&refresh=5s) (계정: `.env` 참조)
-   **수집 데이터**:
    -   **System**: CPU 사용률, 메모리 점유율, Uptime
    -   **Application**: HTTP 요청 수, 평균 응답 속도(Latency), 에러 발생률(Error Rate)
    -   **JVM**: Heap/Non-Heap 메모리 상태, Garbage Collection 로그

### 6.2 CI/CD 파이프라인
GitHub Actions를 통해 코드 변경부터 배포까지의 과정을 자동화했습니다. (`.github/workflows/ci-cd.yml`)
1.  **Build & Test**: `main` 브랜치 Push 시 JDK 21 환경에서 빌드 및 테스트 수행.
2.  **Container Build**: 빌드 성공 시 Docker Image 생성 및 GHCR(GitHub Container Registry) 업로드.
3.  **Deploy**: SSH를 통해 운영 서버에 접속하여 최신 이미지를 Pull 하고 컨테이너를 무중단 재시작 (Rolling Update).

### 6.3 Firebase ML
Firebase ML을 연동하여 다국어 지원 기능을 구현했습니다.
-   **사용법**: 게시글 상세 조회 시 `lang` 파라미터(예: `en`, `ja`, `zh`)를 추가하여 요청.
-   **동작**:
    1.  클라이언트 요청 (`GET /api/posts/1?lang=en`)
    2.  서버에서 `Google Translation API` 호출하여 제목/본문 번역
    3.  번역된 콘텐츠를 응답 본문에 포함하여 반환 (원문 유지)

---

## 7. 한계와 개선 계획
-   **한계**: 단일 DB 인스턴스로 인한 쓰기 부하 병목 가능성.
-   **개선 계획**:
    -   프론트엔드 구현.
    -   AWS S3 연동.
    -   Docker Swarm 구현.
