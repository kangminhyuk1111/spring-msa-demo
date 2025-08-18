package com.example.userservice.dto.response;

import java.time.LocalDateTime;

public record PointResponse(
    Long id, Long userId, Integer balance, LocalDateTime lastUpdated
) {
}
