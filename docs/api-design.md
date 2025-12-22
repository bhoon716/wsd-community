# API Design Guidelines

## 1. 개요
본 문서는 **WSD Community** 프로젝트의 API 설계 원칙과 규격을 정의합니다.
모든 API는 RESTful 원칙을 지향하며, 클라이언트와의 명확한 소통을 위해 일관된 응답 포맷을 사용합니다.

---

## 2. 기본 원칙
1.  **URI는 소문자 사용**: 가독성을 위해 소문자와 하이픈(`-`)을 사용합니다.
2.  **자원 중심 설계**: 동사보다는 명사 위주의 자원(Resource)을 URI로 표현합니다.
3.  **HTTP Method 활용**: `GET`, `POST`, `PATCH`, `DELETE` 등 표준 메서드를 올바르게 사용합니다.
4.  **Stateless**: 서버는 클라이언트의 상태를 저장하지 않으며(`HttpSession` 미사용), JWT 토큰으로 상태를 검증합니다.

---

## 3. 응답 규격 (Response Format)

API 응답은 **성공(Success)**과 **실패(Error)**에 따라 다른 규격을 사용합니다.

### 3-1. 성공 응답 (Success Response)
`CommonResponse` 객체로 정형화된 응답을 반환합니다.

```json
{
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 1,
    "email": "user@example.com"
  }
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `code` | String | 응답 코드 (성공 시 `SUCCESS`) |
| `message` | String | 사용자에게 표시할 수 있는 안내 메시지 |
| `data` | Object | 실제 응답 데이터 (없을 경우 명시적 `null` 또는 생략) |

### 3-2. 에러 응답 (Error Response)
에러 발생 시에는 상세 정보를 포함한 `ErrorResponse` 객체를 반환합니다.

```json
{
  "timestamp": "2025-12-21T18:00:00",
  "path": "/api/users/1",
  "status": 404,
  "code": "U001",
  "message": "사용자를 찾을 수 없습니다.",
  "details": null
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `timestamp` | String | 에러 발생 시간 (ISO 8601) |
| `path` | String | 요청 경로 |
| `status` | int | HTTP 상태 코드 |
| `code` | String | 서비스 정의 에러 코드 (예: `U001`) |
| `message` | String | 에러 상세 메시지 |
| `details` | Object | (선택) 에러 관련 상세 정보 (Map 형태) |

---

## 4. HTTP 상태 코드 (Status Codes)

| 상태 코드 | 의미 | 설명 |
| --- | --- | --- |
| `200 OK` | 성공 | 요청이 성공적으로 처리됨 |
| `201 Created` | 생성됨 | 리소스가 성공적으로 생성됨 |
| `204 No Content` | 내용 없음 | 성공했지만 반환할 데이터가 없음 |
| `400 Bad Request` | 잘못된 요청 | 파라미터 누락, 유효성 검사 실패 등 |
| `401 Unauthorized` | 인증 실패 | 토큰이 없거나 유효하지 않음 |
| `403 Forbidden` | 권한 없음 | 인증되었으나 리소스 접근 권한 부족 |
| `404 Not Found` | 찾을 수 없음 | 리소스가 존재하지 않음 |
| `409 Conflict` | 충돌 | 데이터 중복, 상태 충돌 등 |
| `422 Unprocessable Entity` | 처리 불가 | 요청 내용은 맞으나 처리할 수 없음 |
| `429 Too Many Requests` | 요청 제한 | Rate Limiting 초과 |
| `500 Internal Server Error` | 서버 에러 | 서버 내부 로직 오류 |

---

## 5. 에러 코드 정의 (Error Codes)

### Global (G)
| 코드 | 메시지 | 상태 코드 | 설명 |
| --- | --- | --- | --- |
| `G001` | 내부 서버 오류가 발생했습니다. | 500 | 핸들링되지 않은 서버 예외 |
| `G002` | 잘못된 입력값입니다. | 400 | Validation 실패 등 |
| `G003` | 요청한 리소스를 찾을 수 없습니다. | 404 | 공통 404 에러 |
| `G004` | 요청 횟수가 초과되었습니다. | 429 | API Rate Limit 초과 |

### User (U)
| 코드 | 메시지 | 상태 코드 | 설명 |
| --- | --- | --- | --- |
| `U001` | 사용자를 찾을 수 없습니다. | 404 | 존재하지 않는 사용자 ID |
| `U002` | 이미 존재하는 이메일입니다. | 409 | 회원가입 중복 이메일 |
| `U003` | 비밀번호가 일치하지 않습니다. | 401 | 로그인 정보 불일치 |
| `U004` | 오너 역할로 변경할 수 없습니다. | 400 | 권한 변경 제약 위반 |
| `U005` | 마지막 관리자는 강등할 수 없습니다. | 409 | 유일한 관리자 보호 |
| `U006` | 마지막 오너는 강등할 수 없습니다. | 409 | 유일한 오너 보호 |
| `U007` | 본인의 역할은 변경할 수 없습니다. | 400 | 자기 자신 역할 변경 불가 |
| `U008` | 오너의 역할은 변경할 수 없습니다. | 400 | 오너에 대한 수정 불가 |

### Auth (A)
| 코드 | 메시지 | 상태 코드 | 설명 |
| --- | --- | --- | --- |
| `A001` | 인증되지 않은 사용자입니다. | 401 | 익명 사용자 접근 시도 |
| `A002` | 접근 권한이 없습니다. | 403 | 필요한 Role 부족 |
| `A003` | 유효하지 않은 토큰입니다. | 401 | 위조/손상된 토큰 |
| `A004` | 만료된 토큰입니다. | 401 | JWT 만료 |
| `A005` | 리프레시 토큰을 찾을 수 없습니다. | 404 | 토큰 재발급 실패 |
| `A006` | 관리자 권한이 필요합니다. | 403 | ADMIN 권한 부족 |
| `A007` | 오너 권한이 필요합니다. | 403 | OWNER 권한 부족 |

### Post (P) / Comment (C)
| 코드 | 메시지 | 상태 코드 | 설명 |
| --- | --- | --- | --- |
| `P001` | 게시글을 찾을 수 없습니다. | 404 | 존재하지 않는 게시글 |
| `P002` | 게시글 작성자가 아닙니다. | 403 | 수정/삭제 권한 위반 |
| `C001` | 댓글을 찾을 수 없습니다. | 404 | 존재하지 않는 댓글 |
| `C002` | 댓글 작성자가 아닙니다. | 403 | 수정/삭제 권한 위반 |

### Report (R)
| 코드 | 메시지 | 상태 코드 | 설명 |
| --- | --- | --- | --- |
| `R001` | 신고를 찾을 수 없습니다. | 404 | 존재하지 않는 신고 내역 |
| `R002` | 본인의 게시글이나 댓글은 신고할 수 없습니다. | 400 | 자가 신고 방지 |
| `R003` | 이미 신고한 게시글/댓글입니다. | 409 | 중복 신고 방지 |
| `R004` | 이미 처리된 신고입니다. | 400 | 중복 처리 방지 |
| `R005` | 신고 핸들러 설정 오류입니다. | 500 | 서버 내부 설정 오류 |
| `R006` | 유효하지 않은 신고 처리 작업입니다. | 400 | 잘못된 Action 요청 |
| `R007` | 지원하지 않는 신고 유형입니다. | 400 | 잘못된 신고 사유 |

---

## 6. 인증 및 헤더 (Authentication Headers)
보호된 리소스에 접근할 때는 반드시 `Authorization` 헤더를 포함해야 합니다.

`Authorization: Bearer <ACCESS_TOKEN>`

---

## 7. 날짜 및 시간 포맷
- 모든 날짜와 시간은 **ISO 8601** 형식(`yyyy-MM-dd'T'HH:mm:ss`)을 따릅니다.
- 서버의 시간대는 기본적으로 `Asia/Seoul` (KST)을 기준으로 합니다.

---

## 8. API 엔드포인트 목록

### 8-1. Auth (인증)
| Method | URI | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/auth/firebase` | Firebase ID Token 로그인 |
| `POST` | `/api/auth/reissue` | Access Token 재발급 |
| `POST` | `/api/auth/logout` | 로그아웃 |

### 8-2. User (사용자)
| Method | URI | 설명 |
| :--- | :--- | :--- |
| `GET` | `/api/users/me` | 내 정보 조회 |
| `DELETE` | `/api/users/me` | 회원 탈퇴 |
| `PUT` | `/api/users/{userId}/role` | 회원 역할 변경 (OWNER) |

### 8-3. Post (게시글)
| Method | URI | 설명 |
| :--- | :--- | :--- |
| `GET` | `/api/posts` | 게시글 목록 조회 (검색) |
| `POST` | `/api/posts` | 게시글 생성 |
| `GET` | `/api/posts/my` | 내 게시글 목록 조회 |
| `GET` | `/api/posts/{postId}` | 게시글 상세 조회 |
| `PUT` | `/api/posts/{postId}` | 게시글 수정 |
| `DELETE` | `/api/posts/{postId}` | 게시글 삭제 |
| `POST` | `/api/posts/{postId}/pin` | 게시글 고정 (ADMIN) |
| `POST` | `/api/posts/{postId}/likes` | 게시글 좋아요 토글 |

### 8-4. Comment (댓글)
| Method | URI | 설명 |
| :--- | :--- | :--- |
| `GET` | `/api/posts/{postId}/comments` | 댓글 목록 조회 |
| `POST` | `/api/posts/{postId}/comments` | 댓글 생성 |
| `PUT` | `/api/comments/{commentId}` | 댓글 수정 |
| `DELETE` | `/api/comments/{commentId}` | 댓글 삭제 |
| `POST` | `/api/comments/{commentId}/likes` | 댓글 좋아요 토글 |

### 8-5. Report (신고)
| Method | URI | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/reports` | 신고 생성 |
| `GET` | `/api/reports` | 신고 목록 조회 (ADMIN) |
| `GET` | `/api/reports/me` | 내 신고 목록 조회 |
| `GET` | `/api/reports/{reportId}` | 신고 상세 조회 |
| `PUT` | `/api/reports/{reportId}` | 신고 수정 |
| `DELETE` | `/api/reports/{reportId}` | 신고 취소 |
| `POST` | `/api/reports/{reportId}/process` | 신고 처리 (ADMIN) |

### 8-6. Stats (통계)
| Method | URI | 설명 |
| :--- | :--- | :--- |
| `GET` | `/api/stats/users/top-writers` | 우수 작성자 랭킹 |
| `GET` | `/api/stats/users/top-commenters` | 우수 답변자 랭킹 |

### 8-7. System
| Method | URI | 설명 |
| :--- | :--- | :--- |
| `GET` | `/health` | 서버 헬스 체크 |
