package com.example.pointservice.repository;

import com.example.pointservice.domain.Point;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, Long> {

  boolean existByUserId(Long userId);

  @Modifying
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Point p WHERE p.userId = :userId")
  Optional<Point> findByUserIdWithLock(Long userId);

  Optional<Point> findByUserId(Long userId);
}
