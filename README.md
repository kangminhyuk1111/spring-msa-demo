# Spring MSA Demo

> **멀티서버 아키텍처를 활용한 이커머스 시스템**  
> Spring Boot 기반 마이크로서비스 패턴 학습 프로젝트

## 프로젝트 소개

이 프로젝트는 **마이크로서비스 아키텍처(MSA)** 를 학습하기 위한 실습용 이커머스 시스템입니다.  
각 서비스를 독립적으로 개발하고 API Gateway를 통해 통합하여, 실제 운영 환경과 유사한 멀티서버 구조를 경험할 수 있습니다.

## 시스템 아키텍처

```
Client
    ↓
API Gateway (8080)
    ↓
┌─────────────────────────────────────────┐
│  User Service      Product Service      │
│     (8081)              (8082)          │
│                                         │
│  Order Service                          │
│     (8083)                              │
└─────────────────────────────────────────┘
```

## 기술 스택

- **Backend**: Spring Boot 3.x, Spring Data JPA
- **Database**: H2 (개발용)
- **API Gateway**: Spring Cloud Gateway
- **Inter-Service Communication**: RestTemplate
- **Build Tool**: Gradle
- **Java Version**: 17

## 프로젝트 구조

```
spring-msa-demo/
├── product-service/     # 상품 관리 서비스 (Port: 8082)
├── order-service/       # 주문 관리 서비스 (Port: 8083)
├── user-service/        # 사용자 관리 서비스 (Port: 8081)
├── gateway-service/     # API Gateway (Port: 8080)
├── docker-compose.yml   # 통합 실행 환경
└── README.md
```

## 학습 목표

### MSA 핵심 개념
- [x] **서비스 분리**: 도메인별 독립적인 서비스 구성
- [x] **서비스 간 통신**: REST API를 통한 마이크로서비스 연동
- [ ] **API Gateway**: 단일 진입점을 통한 라우팅
- [ ] **부하 분산**: 동일 서비스의 다중 인스턴스 운영
- [ ] **장애 격리**: Circuit Breaker 패턴 적용

### 실무 스킬
- [ ] **Docker 컨테이너화**: 서비스별 독립 배포
- [ ] **모니터링**: 헬스체크 및 메트릭 수집
- [ ] **성능 테스트**: 부하테스트를 통한 병목점 발견
- [ ] **데이터베이스 분리**: 서비스별 독립 DB 운영

## 구현 미션

### 1단계: Product Service 구현 (완료)
**[상세 요구사항](./docs/1단계.md)**
- [x] Spring Boot 프로젝트 설정 (Port: 8082)
- [x] Product 엔티티 설계 (id, name, price, stock)
- [x] H2 데이터베이스 연동
- [x] CRUD API 구현 (/api/products)
- [x] 단위 테스트 및 API 테스트

### 2단계: Order Service 구현 (진행 중)
**[상세 요구사항](./docs/2단계.md)**
- [x] Spring Boot 프로젝트 설정 (Port: 8083)
- [x] Order 엔티티 설계 (id, productId, quantity, totalPrice, status)
- [x] FeignClient을 통한 Product Service 호출
- [x] 주문 생성 로직 구현 (재고 검증 포함)
- [x] 주문 상태 관리 API

### 3단계: API Gateway 구현 (예정)
**[상세 요구사항](./docs/3단계.md)**
- [ ] Spring Cloud Gateway 설정 (Port: 8080)
- [ ] 서비스별 라우팅 규칙 구성
- [ ] 통합 테스트 환경 구축
- [ ] 부하 분산 테스트

### 4단계: 고급 기능 구현 (예정)
**[상세 요구사항](./docs/4단계.md)**
- [ ] User Service 추가
- [ ] 인증/인가 처리
- [ ] Docker Compose를 통한 통합 배포
- [ ] 성능 테스트 및 최적화

## 실행 방법

### 개별 서비스 실행
```bash
# Product Service 실행
cd product-service
./gradlew bootRun

# Order Service 실행 (다른 터미널)
cd order-service
./gradlew bootRun
```

### 서비스 접근
- **Product Service**: http://localhost:8082/api/products
- **Order Service**: http://localhost:8083/api/orders
- **H2 Console**: http://localhost:808x/h2-console

## API 테스트

### Product Service
```bash
# 상품 등록
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "노트북", "price": 1000000, "stock": 10}'

# 상품 조회
curl http://localhost:8082/api/products
```

### Order Service
```bash
# 주문 생성
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'
```

## 학습 노트

이 프로젝트를 통해 학습한 내용들을 정리합니다:

- **MSA의 장단점**: 서비스 독립성 vs 복잡성 증가
- **서비스 간 통신**: 동기 vs 비동기 통신 패턴
- **데이터 일관성**: 분산 환경에서의 트랜잭션 관리
- **장애 대응**: 서비스 장애가 전체 시스템에 미치는 영향

## 기여하기

이 프로젝트는 학습 목적으로 제작되었습니다. 개선 사항이나 추가 기능에 대한 아이디어가 있으시면 언제든 Issue나 PR을 남겨주세요!

---

**관련 링크**
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [마이크로서비스 패턴](https://microservices.io/)
