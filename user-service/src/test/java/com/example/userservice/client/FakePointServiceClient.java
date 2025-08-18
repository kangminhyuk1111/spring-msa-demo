package com.example.userservice.client;

import com.example.userservice.dto.response.PointResponse;
import java.time.LocalDateTime;

public class FakePointServiceClient implements PointServiceClient {

  @Override
  public PointResponse createAccount(final Long userId) {
    return new PointResponse(1L, userId, 0, LocalDateTime.now());
  }
}
