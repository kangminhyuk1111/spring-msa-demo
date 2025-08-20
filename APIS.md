# MVP 우선순위별 마이크로서비스 API

## 🚀 Phase 1 - 핵심 기능 (최우선 구현)

### 1. User Service (회원 서비스) - Phase 1
```
✅ 필수 구현
- POST /api/users/register - 회원 가입
- POST /api/users/login - 로그인 (JWT 반환)
- GET /api/users/profile - 내 프로필 조회

🔧 내부 API
- GET /internal/users/{id} - 회원 정보 조회 (이름만)
- POST /internal/users/validate - 회원 ID 유효성 검증
```

### 2. Product Service (상품 서비스) - Phase 1
```
✅ 필수 구현
- GET /api/products - 상품 목록 조회 (기본 페이징)
- GET /api/products/{id} - 상품 상세 조회
- POST /api/products - 상품 등록 (관리자용)

🔧 내부 API
- GET /internal/products/{id} - 상품 정보 조회 (이름, 가격, 재고)
- POST /internal/products/decrease-stock - 재고 차감
```

### 3. Order Service (주문 서비스) - Phase 1
```
✅ 필수 구현
- POST /api/orders - 주문 생성 (기본 기능만)
- GET /api/orders - 내 주문 목록 조회
- GET /api/orders/{id} - 주문 상세 조회

🔧 내부 API
- GET /internal/orders/{id} - 주문 정보 조회 (member_id, total_price)
```

---

## 📈 Phase 2 - 기본 기능 확장

### 4. Point Service (포인트 서비스) - Phase 2
```
✅ 기본 구현
- GET /api/points - 내 포인트 조회
- GET /api/points/history - 내 포인트 사용 내역

🔧 내부 API
- GET /internal/points/{userId} - 포인트 잔액 조회
- POST /internal/points/earn - 포인트 적립 (주문 완료시)
- POST /internal/points/use - 포인트 사용 처리
```

### User Service - Phase 2 확장
```
📝 추가 구현
- PUT /api/users/profile - 내 프로필 수정
- POST /api/users/auth/refresh - 토큰 갱신
```

### Product Service - Phase 2 확장
```
📝 추가 구현
- GET /api/products/search?name={검색어} - 상품 검색
- PUT /api/products/{id} - 상품 정보 수정
- PUT /api/products/{id}/stock - 재고 수정

🔧 내부 API 확장
- POST /internal/products/check-stock - 재고 확인 (여러 상품)
- POST /internal/products/increase-stock - 재고 복원 (주문 취소시)
```

### Order Service - Phase 2 확장
```
📝 추가 구현
- PUT /api/orders/{id}/cancel - 주문 취소
- PUT /api/orders/{id}/status - 주문 상태 변경 (관리자용)
```

---

## 🎯 Phase 3 - 관리 기능

### User Service - Phase 3 (관리자 기능)
```
🔧 관리자 기능
- GET /api/users/members - 회원 목록 조회 (관리자용)
- GET /api/users/members/{id} - 회원 정보 조회 (관리자용)
- DELETE /api/users/members/{id} - 회원 탈퇴 (관리자용)
```

### Order Service - Phase 3 (관리/통계)
```
🔧 관리자 기능
- GET /api/orders/admin - 전체 주문 목록 조회 (관리자용)
- GET /api/orders/stats/daily - 일별 주문 통계
- GET /api/orders/members/{memberId} - 회원별 주문 목록 조회
```

### Point Service - Phase 3 (관리/통계)
```
🔧 관리자 기능
- POST /api/points/users/{userId}/add - 포인트 수동 적립 (관리자용)
- GET /api/points/stats/total - 전체 포인트 통계
- GET /api/points/users/{userId}/stats - 사용자별 포인트 통계
```

---

## 🚫 MVP에서 제외된 기능들 (나중에 구현)

### 제외된 복잡한 기능들
```
❌ 나중에 구현
- 재고 예약/해제 시스템 (Phase 1에서는 단순 차감만)
- 상품 카테고리 관리
- 주문 항목 개별 수정
- 포인트 정책 관리
- 이메일로 회원 조회
- 로그아웃 처리 (JWT는 stateless)
- 월별 통계 등 상세 통계
```

---

## 📋 구현 순서 가이드

### 1주차: User Service (Phase 1)
- 회원가입, 로그인, 프로필 조회
- JWT 토큰 발행/검증
- 내부 API (회원 정보 조회)

### 2주차: Product Service (Phase 1)
- 상품 CRUD 기본 기능
- 재고 관리 (단순 차감)
- 내부 API (상품 정보, 재고 차감)

### 3주차: Order Service (Phase 1)
- 주문 생성 (User/Product Service 연동)
- 주문 조회
- 내부 API

### 4주차: Point Service (Phase 2)
- 포인트 조회, 사용 내역
- 주문 완료시 포인트 적립 연동
- Order Service와 연동

### 5주차 이후: Phase 2, 3 순차 구현

---

## 🔄 MVP 주문 플로우 (간소화)

```
1. 클라이언트 → Order Service: POST /api/orders
   {
     "items": [{"product_id": 1, "quantity": 2}]
   }

2. Order Service → User Service: GET /internal/users/{member_id}
   (토큰에서 member_id 추출하여 회원 확인)

3. Order Service → Product Service: GET /internal/products/{id}
   (상품 정보 및 재고 확인)

4. Order Service → Product Service: POST /internal/products/decrease-stock
   (재고 차감 - 실패시 주문 실패)

5. Order Service → DB: 주문 저장

6. Order Service → 클라이언트: 주문 결과 반환

7. [Phase 2에서 추가] Order Service → Point Service: POST /internal/points/earn
   (포인트 적립)
```

---

## 🛡️ MVP 보안 및 인증 (단순화)

### JWT 토큰 (Phase 1)
- 로그인시 JWT 발행 (유효기간 24시간)
- 모든 인증 필요 API에 Bearer 토큰 필요
- 토큰 만료시 재로그인 (refresh 토큰은 Phase 2)

### 권한 관리 (Phase 1)
- 토큰 payload에 role 포함 (user, admin)
- 관리자 API는 admin role 확인

### 기본 보안
- HTTPS 사용
- 기본적인 입력 검증
- SQL Injection 방지

이렇게 하면 **핵심 기능을 빠르게 구현**하고, 점진적으로 확장할 수 있습니다!