# WSD Community Project

> **2025-2학기 Web System Design Term Project**  
> 고성능, 고가용성을 지향하는 동아리 커뮤니티 백엔드 서비스

---

## 1. 프로젝트 개요 (Project Overview)
**WSD Community**는 대학 동아리 운영을 위한 올인원 커뮤니티 플랫폼의 백엔드 시스템입니다.  
단순한 게시판 기능을 넘어, **강력한 보안(RBAC)**, **대용량 트래픽 대응 설계**를 목표로 개발되었습니다.

### 주요 기능
-   **게시판**: 일반/질문 게시글 작성, 조회(검색/정렬/페이징), 수정, 삭제.
-   **댓글**: 게시글에 대한 댓글 작성 및 조회, 대댓글(선택적).
-   **신고 시스템**: 부적절한 게시글/댓글 신고 및 관리자 처리 프로세스.
-   **게시글 번역**: Google Cloud Translation API를 이용한 AI 자동 번역 (다국어 지원).
-   **통계**: 활동량이 많은 우수 작성자/답변자 랭킹 집계.
-   **인증/인가**: JWT 기반 자체 로그인 및 Firebase 소셜 로그인, RBAC(User/Admin/Owner) 권한 관리.

---

## 2. 실행 방법 (Get Started)

### 사전 요구사항 (Prerequisites)
-   Docker & Docker Compose
-   JDK 21 (로컬 빌드/실행 시)

### 1) Docker Compose 실행 (권장)
데이터베이스(MySQL, Redis)와 애플리케이션을 통합 실행합니다.

```bash
# 1. 환경변수 파일 생성
cp .env.example .env

# 2. 실행
docker compose up -d --build
```
-   **서버 접속**: `http://localhost:8080`
-   **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`

### 2) 로컬 실행 (Gradle)
DB만 Docker로 띄우고 애플리케이션은 로컬 환경에서 실행합니다.

```bash
# 1. DB 실행
docker compose up -d db redis

# 2. 의존성 설치 및 실행
./gradlew clean bootRun
```

### 3) 테스트 실행
```bash
./gradlew test
```

---

## 3. 환경변수 설명 (Environment Variables)
`.env.example` 파일을 참고하여 `.env` 파일을 생성해야 합니다.

| 변수명 | 설명 | 비고 |
| --- | --- | --- |
| `MYSQL_USER` | DB 사용자명 | |
| `MYSQL_PASSWORD` | DB 비밀번호 | |
| `MYSQL_ROOT_PASSWORD` | DB 루트 비밀번호 | Docker 컨테이너용 |
| `JWT_SECRET` | JWT 서명 키 | **32자 이상 필수** |
| `SPRING_DATA_REDIS_PASSWORD` | Redis 비밀번호 | |
| `FIREBASE_CONFIG` | Firebase Admin SDK 설정 (JSON) | 소셜 로그인용 |

---

## 4. 배포 주소 (Deployment)
(JCloud 배포 환경 기준)

-   **Base URL**: `http://<SERVER_IP>:8080`
-   **Swagger UI**: `http://<SERVER_IP>:8080/swagger-ui/index.html`
-   **Health Check**: `http://<SERVER_IP>:8080/health`

---

## 5. Postman 환경설정 (Postman Configuration)
제출된 Postman Collection(`postman/wsd_community_collection.json`)을 사용하기 위한 환경 변수 설정입니다.
`Environments` 또는 `Collection Variables`에 아래 변수를 설정해주세요.

| 변수명 | 값 (예시) | 설명 |
| --- | --- | --- |
| `baseUrl` | `http://localhost:8080` | API 서버 주소 |
| `firebase_api_key` | `AIzaSy...` | Firebase Web API Key (프로젝트 설정 > 일반 > 웹 API 키) |

> **참고**: `accessToken` 및 `idToken` 등은 로그인 요청 시 **Pre-request/Tests 스크립트**에 의해 자동으로 설정됩니다. 별도로 입력할 필요가 없습니다.

---

## 6. 인증 플로우 설명 (Auth Flow)
1.  **로그인**: 
    -   소셜 로그인: `POST /api/auth/firebase` (Firebase ID Token)
    -   OAuth2 로그인: `GET /oauth2/authorization/google` (Spring Security OAuth2)
2.  **토큰 발급**: 
    -   인증 성공 시 `Access Token` (유효기간 1시간) 및 `Refresh Token` (유효기간 7일) 발급.
3.  **요청 인증**: 
    -   보호된 API 요청 시 헤더에 `Authorization: Bearer <Access Token>` 포함.
4.  **토큰 재발급**: 
    -   Access Token 만료 시 `POST /api/auth/reissue` 요청. Refresh Token Rotation(RTR) 적용.

---

## 7. 역할/권한표 (Roles & Permissions)

| Role | 설명 | 접근 가능 범위 |
| --- | --- | --- |
| **USER** | 일반 사용자 | 게시글/댓글 작성, 본인 글 수정/삭제, 좋아요, 신고 |
| **ADMIN** | 관리자 | 모든 글/댓글 관리(삭제/수정), 신고 목록 조회 및 처리, 게시글 고정 |
| **OWNER** | 최고 관리자 | 관리자 권한 + 시스템 설정 및 오너 전용 기능 |

---

## 8. 예제 계정 (Example Accounts)
초기 데이터(`V2__seed_data.sql`)에 포함된 테스트용 계정입니다.

| Role | Email | Password |
| --- | --- | --- |
| **USER** | `user1@test.com` | `password1234` |
| **ADMIN** | `admin@test.com` | `password1234` |
| **OWNER** | `owner@test.com` | `password1234` |

---

## 9. DB 연결 정보 (Database)
로컬 및 테스트 환경 접속 정보입니다.

-   **Host**: `localhost` (Docker Network 내부: `mysql`)
-   **Port**: `3306`
-   **Database**: `community`
-   **User**: `user` (환경변수 참조)
-   **Password**: `password` (환경변수 참조)

---

## 10. 엔드포인트 요약표 (API Endpoints)

| Method | URI | 설명 | 권한 |
| :--- | :--- | :--- | :--- |
| `GET` | `/health` | 헬스 체크 | ALL |
| `POST` | `/api/auth/firebase` | Firebase 로그인 | ALL |
| `GET` | `/api/posts` | 게시글 목록 (검색/정렬) | ALL |
| `POST` | `/api/posts` | 게시글 작성 | USER+ |
| `GET` | `/api/posts/{id}` | 게시글 상세 조회 | ALL |
| `DELETE` | `/api/posts/{id}` | 게시글 삭제 | 본인/ADMIN |
| `POST` | `/api/reports` | 신고 생성 | USER+ |
| `GET` | `/api/reports` | 신고 목록 조회 (필터링 가능) | ADMIN+ |
| `POST` | `/api/reports/{id}/process` | 신고 처리 | ADMIN+ |
| `GET` | `/api/stats/users/top-writers` | 우수 작성자 랭킹 | ALL |

---

## 11. 성능 및 보안 고려사항 (Performance & Security)
1.  **Rate Limiting**: `Bucket4j`를 적용하여 IP 기반 요청 제한 (DDoS 방지).
2.  **N+1 문제 해결**: `Fetch Join` 및 `default_batch_fetch_size` 설정을 통해 연관 관계 조회 성능 최적화.
3.  **인덱싱 (Indexing)**: 검색 성능 향상을 위해 `users(email)`, `posts(created_at, title)` 등 주요 컬럼에 인덱스 적용.
4.  **보안**:
    -   모든 비밀번호는 `BCrypt`로 해싱 저장.
    -   `Spring Security` + `JWT` 필터를 통한 철저한 인증/인가.
    -   `CorsConfigurationSource`를 통한 명시적 CORS 정책 적용.

---

## 12. 한계와 개선 계획 (Limitations)
-   **한계**: 단일 MySQL 인스턴스 사용으로 인한 쓰기 작업 병목 가능성 존재.
-   **개선 계획**:
    -   DB Replication (Master-Slave) 구조 도입으로 읽기 성능 분산.
    -   Redis 캐싱 전략을 단순 조회(Lookup)에서 Write-Back 등으로 고도화.
    -   CI/CD 파이프라인(Github Actions) 구축을 통한 배포 자동화.

---

## 13. CI/CD 파이프라인 (Automated Deployment)
GitHub Actions를 통해 빌드, 테스트, 배포 과정을 자동화했습니다. (`.github/workflows/ci-cd.yml`)

### Pipeline Stages
1.  **CI (Build & Test)**:
    -   `main` 브랜치 Push/PR 시 트리거.
    -   JDK 21 환경에서 `./gradlew build` 수행 (Unit Test & Checkstyle 포함).
    -   Docker Compose를 이용해 테스트용 DB(MySQL, Redis) 자동 프로비저닝.
2.  **Container Build & Push**:
    -   빌드 성공 시 Docker Image 생성 (`ghcr.io/repo/community-app`).
    -   GitHub Container Registry (GHCR)에 이미지 업로드.
3.  **CD (Deployment)**:
    -   `SSH`를 통해 운영 서버에 접속.
    -   최신 Docker Image를 Pull 하고 `docker compose up -d`로 무중단 배포(Rolling Update 효과).
    -   **Health Check**: 배포 후 `/health` 또는 `/actuator/health` 엔드포인트를 호출하여 정상 구동 확인 (최대 2분 대기).

---

## 14. 에러 코드 명세 (Error Codes)

### 공통 응답 포맷 (JSON)
API는 예외 발생 시 일관된 포맷의 JSON 응답을 반환합니다.

```json
{
  "timestamp": "2025-12-21T18:00:00",
  "path": "/api/posts/999",
  "status": 404,
  "code": "P001",
  "message": "게시글을 찾을 수 없습니다.",
  "details": null
}
```

### 주요 에러 코드
| HTTP | Code | 설명 |
| --- | --- | --- |
| **404** | `U001` | 사용자를 찾을 수 없습니다. |
| **409** | `U002` | 이미 존재하는 이메일입니다. |
| **401** | `U003` | 비밀번호가 일치하지 않습니다. |
| **401** | `A001` | 인증되지 않은 사용자입니다. |
| **403** | `A002` | 접근 권한이 없습니다. |
| **401** | `A003` | 유효하지 않은 토큰입니다. |
| **401** | `A004` | 만료된 토큰입니다. |
| **404** | `A005` | 리프레시 토큰을 찾을 수 없습니다. |
| **403** | `A006` | 관리자 권한이 필요합니다. |
| **403** | `A007` | 오너 권한이 필요합니다. |
| **404** | `P001` | 게시글을 찾을 수 없습니다. |
| **403** | `P002` | 게시글 작성자가 아닙니다. |
| **404** | `C001` | 댓글을 찾을 수 없습니다. |
| **403** | `C002` | 댓글 작성자가 아닙니다. |
| **404** | `R001` | 신고를 찾을 수 없습니다. |
| **400** | `R002` | 본인의 게시글이나 댓글은 신고할 수 없습니다. |
| **409** | `R003` | 이미 신고한 게시글/댓글입니다. |
| **400** | `R004` | 이미 처리된 신고입니다. |
| **500** | `R005` | 신고 핸들러 설정 오류입니다. |
| **400** | `R006` | 유효하지 않은 신고 처리 작업입니다. |
| **400** | `R007` | 지원하지 않는 신고 유형입니다. |
| **500** | `G001` | 내부 서버 오류가 발생했습니다. |
| **400** | `G002` | 잘못된 입력값입니다. |
| **404** | `G003` | 요청한 리소스를 찾을 수 없습니다. |
| **429** | `G004` | 요청 횟수가 초과되었습니다. |
| **400** | `U004` | 오너 역할로 변경할 수 없습니다. |
| **409** | `U005` | 마지막 관리자는 강등할 수 없습니다. |
| **409** | `U006` | 마지막 오너는 강등할 수 없습니다. |
| **400** | `U007` | 본인의 역할은 변경할 수 없습니다. |
| **400** | `U008` | 오너의 역할은 변경할 수 없습니다. |

---

## 15. Firebase ML (AI Translation)
Google Cloud Translation API를 활용하여 게시글 제목과 본문을 원하는 언어로 자동 번역합니다. (Firebase ML & AI)

### 동작 원리
1.  사용자가 게시글 상세 조회 시 `lang` 파라미터 전달 (예: `/api/posts/1?lang=en`).
2.  서버에서 **Google Cloud Translation API** (`translate`) 호출.
3.  게시글의 제목과 본문을 해당 언어(`en`)로 번역.
4.  번역된 텍스트를 응답에 포함하여 반환. (번역 API 실패 시 원문 반환 - Fail-open)

### 사용 예시
-   **요청**: `GET /api/posts/1?lang=en`
-   **응답**:
    ```json
    {
        "id": 1,
        "title": "Hello World",
        "content": "This is a translated post.",
        ...
    }
    ```
