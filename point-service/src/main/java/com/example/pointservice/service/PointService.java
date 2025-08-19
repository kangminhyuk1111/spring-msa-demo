package com.example.pointservice.service;

import com.example.pointservice.domain.Point;
import com.example.pointservice.dto.request.AddPointRequest;
import com.example.pointservice.dto.request.RefundPointRequest;
import com.example.pointservice.dto.response.PointResponse;
import com.example.pointservice.dto.request.UsePointRequest;
import com.example.pointservice.exception.PointException;
import com.example.pointservice.repository.PointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

  private static final Logger log = LoggerFactory.getLogger(PointService.class);
  private static final int INITIAL_POINTS = 0;

  private final PointRepository pointRepository;

  public PointService(final PointRepository pointRepository) {
    this.pointRepository = pointRepository;
  }

  public PointResponse findPointByUserId(final Long userId) {
    final Point point = pointRepository.findByUserId(userId)
        .orElseThrow(() -> new PointException("계좌가 존재하지 않습니다."));

    return PointResponse.of(point);
  }

  /**
   * 포인트 추가
   * 계좌가 없으면 새로 생성 후 포인트 추가
   */
  @Transactional
  public PointResponse addPoint(final AddPointRequest request) {
    final Point point = findOrCreatePointAccount(request.userId());

    point.addPoint(request.amount());

    log.info("포인트 추가 완료 - userId: {}, 추가금액: {}, 잔여포인트: {}",
        request.userId(), request.amount(), point.getBalance());

    return PointResponse.of(point);
  }

  /**
   * 포인트 차감
   * 계좌가 없으면 새로 생성 후 차감 시도 (잔액 부족으로 실패할 것임)
   */
  @Transactional
  public PointResponse usePoint(final UsePointRequest request) {
    // 계좌가 없으면 새로 생성 (잔액 0으로 시작)
    final Point point = findOrCreatePointAccount(request.userId());

    // 도메인 객체에서 잔액 확인 및 차감 처리
    point.usePoint(request.amount());

    log.info("포인트 차감 완료 - userId: {}, 차감금액: {}, 잔여포인트: {}",
        request.userId(), request.amount(), point.getBalance());

    return PointResponse.of(point);
  }

  /**
   * 포인트 환불
   * 계좌가 반드시 존재해야 함 (환불은 기존 거래에 대한 것이므로)
   */
  @Transactional
  public PointResponse refundPoint(final RefundPointRequest request) {
    final Point point = pointRepository.findByUserIdWithLock(request.userId())
        .orElseThrow(() -> new PointException("계좌가 존재하지 않습니다."));

    point.refundPoint(request.balance());

    log.info("포인트 환불 완료 - userId: {}, 환불금액: {}, 잔여포인트: {}",
        request.userId(), request.balance(), point.getBalance());

    return PointResponse.of(point);
  }

  /**
   * 포인트 사용 가능 여부 확인
   * 계좌가 없으면 false 반환
   */
  public boolean canUsePoint(final Long userId, final Integer amount) {
    return pointRepository.findByUserId(userId)
        .map(point -> point.getBalance() >= amount)
        .orElse(false);
  }

  /**
   * 포인트 계좌 생성
   */
  @Transactional
  public PointResponse createPointAccount(final Long userId) {
    if (pointRepository.findByUserId(userId).isPresent()) {
      throw new PointException("이미 포인트 계좌가 존재합니다.");
    }

    final Point newAccount = createNewPointAccount(userId);
    return PointResponse.of(newAccount);
  }

  private Point findOrCreatePointAccount(Long userId) {
    return pointRepository.findByUserIdWithLock(userId)
        .orElseGet(() -> createNewPointAccount(userId));
  }

  private Point createNewPointAccount(Long userId) {
    log.info("포인트 계좌가 없어 새로 생성합니다. userId: {}", userId);

    try {
      Point newAccount = Point.openAccount(userId);
      Point savedPoint = pointRepository.save(newAccount);

      log.info("포인트 계좌 생성 완료 - userId: {}, 초기포인트: {}",
          userId, INITIAL_POINTS);

      return savedPoint;

    } catch (Exception e) {
      log.error("포인트 계좌 생성 실패: userId={}", userId, e);
      throw new PointException("포인트 계좌 생성에 실패했습니다", e);
    }
  }
}