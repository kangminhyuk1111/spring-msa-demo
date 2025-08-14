package com.example.pointservice;

import com.example.pointservice.dto.request.CreateAccountRequest;
import com.example.pointservice.dto.response.PointResponse;
import com.example.pointservice.service.PointService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
public class PointController {

  private final PointService pointService;

  public PointController(final PointService pointService) {
    this.pointService = pointService;
  }

  @PostMapping("/create")
  public PointResponse createAccount(@RequestBody CreateAccountRequest request) {
    return pointService.createAccount(request);
  }
}
