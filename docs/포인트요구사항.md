# Point Service 구현

## 기본 설정
- **포트**: `8084`
- **데이터베이스**: H2 (인메모리)
- **Base URL**: `/api/points`

## 포인트 엔티티
다음 필드를 가진 Point 엔티티 구현:
- `id` (Long, 자동 증가)
- `userId` (Long, 필수) - 사용자 ID
- `balance` (Integer, 필수) - 현재 포인트 잔액
- `lastUpdated` (LocalDateTime, 필수) - 마지막 갱신 시각

## API 엔드포인트

| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | `/api/points/{userId}` | 특정 사용자 포인트 조회 |
| POST | `/api/points/{userId}/earn` | 포인트 적립 |
| POST | `/api/points/{userId}/use` | 포인트 사용 |
| POST | `/api/points/{userId}/refund` | 사용 포인트 환불 |

## 서비스 간 통신
- **Order Service 호출** (선택)  
  포인트 사용 시, 실제 주문이 존재하는지 확인 가능
- **호출 URL**: `http://localhost:8083/api/orders/{orderId}`
- **사용 기술**: `RestTemplate` 또는 `WebClient`

## 포인트 적립 로직
1. `userId`로 포인트 엔티티 조회 (없으면 새로 생성, balance = 0)
2. `balance`에 적립 금액 추가
3. `lastUpdated` 현재 시간으로 변경
4. 저장 후 결과 반환

## 포인트 사용 로직
1. `userId`로 포인트 조회
2. 현재 잔액 >= 사용 금액인지 확인
3. 차감 후 저장
4. 실패 시 예외 발생

## 포인트 환불 로직
1. `userId`로 포인트 조회
2. 환불 금액을 `balance`에 추가
3. 저장

## 테스트 조건
- 서버 실행 후 `http://localhost:8084/api/points` 접근 가능
- Order Service와 Point Service 동시 실행 가능
- 실제 주문을 기반으로 포인트 사용/환불 테스트 가능

## 완료 기준
- ✅ 서버가 8084 포트에서 정상 실행
- ✅ 포인트 적립/사용/환불 로직 동작
- ✅ 예외 처리 및 유효성 검사
- ✅ 모든 API 엔드포인트 정상 동작
