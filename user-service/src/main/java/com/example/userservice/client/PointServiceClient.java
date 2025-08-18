package com.example.userservice.client;

import com.example.userservice.dto.response.PointResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${point-service.name}", url = "${point-service.api-url}")
public interface PointServiceClient {

  @PostMapping
  PointResponse createAccount(@RequestBody Long userId);
}
