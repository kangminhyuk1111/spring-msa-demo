# 포인트 서비스 API 명세서

## 개요
포인트 시스템의 기본 기능(계좌 생성, 포인트 추가, 차감, 조회)을 제공하는 REST API입니다.

### 1. 포인트 계좌 조회

**GET** `/points/accounts/{userId}`

사용자의 포인트 계좌 정보를 조회합니다.

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| userId | Long | Yes | 사용자 ID |

#### Response
**200 OK**
```json
{
  "userId": 1,
  "balance": 5000
}
```

**404 Not Found**
```json
{
  "status": 404,
  "message": "계좌가 존재하지 않습니다.",
  "timestamp": "2024-03-15T10:30:45"
}
```

#### 플로우
1. 사용자 ID로 계좌 조회
2. 계좌가 있으면 정보 반환
3. 계좌가 없으면 예외 발생

---

### 2. 포인트 추가

**POST** `/points/add`

포인트를 추가합니다. 계좌가 없으면 자동으로 생성됩니다.

#### Request Body
```json
{
  "userId": 1,
  "amount": 5000
}
```

#### Response
**200 OK**
```json
{
  "userId": 1,
  "balance": 5000
}
```

**400 Bad Request**
```json
{
  "status": 400,
  "message": "추가할 포인트는 0보다 커야 합니다.",
  "timestamp": "2024-03-15T10:30:45"
}
```

#### 플로우
1. 사용자 ID로 계좌 조회 (Lock)
2. 계좌가 없으면 새로 생성 (초기 잔액 0)
3. 요청된 포인트를 기존 잔액에 추가
4. 업데이트된 잔액 반환

#### 시나리오 예시

**케이스 1: 계좌가 없는 경우**
```
요청: userId=1, amount=5000
1. 계좌 조회 → 없음
2. 새 계좌 생성 → balance=0
3. 포인트 추가 → balance=0+5000=5000
응답: balance=5000
```

**케이스 2: 계좌가 있는 경우**
```
요청: userId=1, amount=2000 (기존 잔액: 3000)
1. 계좌 조회 → balance=3000
2. 포인트 추가 → balance=3000+2000=5000
응답: balance=5000
```

---

### 3. 포인트 차감

**POST** `/points/use`

포인트를 차감합니다. 계좌가 없으면 자동으로 생성 후 잔액 부족으로 실패합니다.

#### Request Body
```json
{
  "userId": 1,
  "amount": 2000
}
```

#### Response
**200 OK**
```json
{
  "userId": 1,
  "balance": 3000
}
```

**400 Bad Request**
```json
{
  "status": 400,
  "message": "포인트가 부족합니다.",
  "timestamp": "2024-03-15T10:30:45"
}
```

#### 플로우
1. 사용자 ID로 계좌 조회 (Lock)
2. 계좌가 없으면 새로 생성 (초기 잔액 0)
3. 잔액이 충분한지 확인
4. 충분하면 차감, 부족하면 예외 발생
5. 업데이트된 잔액 반환

#### 시나리오 예시

**케이스 1: 계좌가 없는 경우**
```
요청: userId=1, amount=1000
1. 계좌 조회 → 없음
2. 새 계좌 생성 → balance=0
3. 잔액 확인 → 0 < 1000 (부족)
응답: 400 Bad Request (포인트가 부족합니다)
```

**케이스 2: 잔액이 충분한 경우**
```
요청: userId=1, amount=2000 (기존 잔액: 5000)
1. 계좌 조회 → balance=5000
2. 잔액 확인 → 5000 >= 2000 (충분)
3. 포인트 차감 → balance=5000-2000=3000
응답: balance=3000
```

**케이스 3: 잔액이 부족한 경우**
```
요청: userId=1, amount=5000 (기존 잔액: 3000)
1. 계좌 조회 → balance=3000
2. 잔액 확인 → 3000 < 5000 (부족)
응답: 400 Bad Request (포인트가 부족합니다)
```

---

### 5. 포인트 사용 가능 여부 확인

**GET** `/points/can-use/{userId}/{amount}`

특정 금액의 포인트 사용이 가능한지 확인합니다.

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| userId | Long | Yes | 사용자 ID |
| amount | Integer | Yes | 확인할 포인트 금액 |

#### Response
**200 OK**
```json
{
  "canUse": true,
  "currentBalance": 5000,
  "requestAmount": 3000
}
```

#### 플로우
1. 사용자 ID로 계좌 조회 (Lock 없음)
2. 계좌가 없으면 false 반환
3. 계좌가 있으면 잔액과 요청 금액 비교
4. 잔액 >= 요청 금액이면 true, 아니면 false

---

## 에러 코드

| Code | HTTP Status | Message Example |
|------|------------|-----------------|
| 404 | Not Found | 계좌가 존재하지 않습니다 |
| 400 | Bad Request | 이미 포인트 계좌가 존재합니다 |
| 400 | Bad Request | 포인트가 부족합니다 |
| 400 | Bad Request | 추가할 포인트는 0보다 커야 합니다 |
| 500 | Internal Server Error | 포인트 계좌 생성에 실패했습니다 |

### 에러 응답 예시
```json
{
  "status": 400,
  "message": "포인트가 부족합니다.",
  "timestamp": "2024-03-15T10:30:45"
}
```

---

## 데이터 모델

### PointResponse
```json
{
  "userId": 1,
  "balance": 5000
}
```

### AddPointRequest
```json
{
  "userId": 1,
  "amount": 5000
}
```

### UsePointRequest
```json
{
  "userId": 1,
  "amount": 2000
}
```

---

## 동시성 제어

- 포인트 추가/차감 시 **Pessimistic Lock** 사용
- 동시에 같은 계좌에 대한 요청이 들어와도 데이터 일관성 보장
- 계좌 조회 시에는 Lock 없이 빠른 응답 제공

---

## 주의사항

1. **계좌 자동 생성**: 포인트 추가/차감 시 계좌가 없으면 자동으로 생성됩니다
2. **초기 잔액**: 새로 생성된 계좌의 초기 잔액은 0입니다
3. **트랜잭션**: 모든 포인트 변경 작업은 트랜잭션 내에서 처리됩니다
4. **Lock**: 포인트 변경 시 Pessimistic Lock을 사용하여 동시성을 제어합니다