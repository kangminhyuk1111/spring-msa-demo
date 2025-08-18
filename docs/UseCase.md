# 상품 주문 케이스

## Phase 1 - 접근 및 인증 인가
1. 회원가입 - POST /api/members/register
2. 로그인 - POST /api/members/login

## Phase 2 - 상품 탐색
1. 상품 목록 조회 - GET /api/products
2. 상품 상세 조회 - GET /api/products/{productId}

## Phase 3 - 주문 생성
1. 주문 생성 요청 - POST /api/orders

## Phase 4 - 결제 처리
1. 주문 상세 확인 - GET /api/orders/{orderId}
2. 포인트 잔액 조회 - GET /api/points/members/{memberId}/balance
3. 결제 처리 - POST /api/orders/{orderId}/payment

## Phase 5 - 주문 완료 및 관리
1. 주문 내역 조회 - GET /api/orders/members/{memberId}
2. 주문 취소 (필요시) - PUT /api/orders/{orderId}/cancel