package com.example.pointservice.service;

import com.example.pointservice.domain.Point;
import com.example.pointservice.dto.request.AddPointRequest;
import com.example.pointservice.dto.request.CreateAccountRequest;
import com.example.pointservice.dto.request.RefundPointRequest;
import com.example.pointservice.dto.response.PointResponse;
import com.example.pointservice.dto.request.UsePointRequest;
import com.example.pointservice.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

  private final PointRepository pointRepository;

  public PointService(final PointRepository pointRepository) {
    this.pointRepository = pointRepository;
  }

  public PointResponse findPointByUserId(final Long id) {
    final Point point = pointRepository.findByUserId(id)
        .orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

    return PointResponse.of(point);
  }

  @Transactional
  public PointResponse createAccount(final CreateAccountRequest request) {
    if (pointRepository.existByUserId(request.userId())) {
      throw new RuntimeException("이미 포인트 계좌가 존재합니다.");
    }

    final Point point = Point.openAccount(request.userId());

    return PointResponse.of(pointRepository.save(point));
  }

  @Transactional
  public PointResponse addPoint(final AddPointRequest request) {
    final Point point = pointRepository.findByUserIdWithLock(request.userId())
        .orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

    point.addPoint(request.balance());

    return PointResponse.of(point);
  }

  @Transactional
  public PointResponse usePoint(final UsePointRequest request) {
    final Point point = pointRepository.findByUserIdWithLock(request.userId())
        .orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

    point.usePoint(request.balance());

    return PointResponse.of(point);
  }

  @Transactional
  public PointResponse refundPoint(final RefundPointRequest request) {
    final Point point = pointRepository.findByUserIdWithLock(request.userId())
        .orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

    point.refundPoint(request.balance());

    return PointResponse.of(point);
  }
}
