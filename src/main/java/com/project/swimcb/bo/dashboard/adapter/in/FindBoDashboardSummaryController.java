package com.project.swimcb.bo.dashboard.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/dashboard/summary")
@RequiredArgsConstructor
public class FindBoDashboardSummaryController {

  @Operation(summary = "대시보드 - 예약, 취소, 환불 현황 조회")
  @GetMapping
  public FindBoDashboardSummaryResponse findBoDashboardSummary() {
    return FindBoDashboardSummaryResponse.builder()
        .reservationRequestCount(3)
        .cancellationProcessedCount(1)
        .refundProcessedCount(1)
        .build();
  }
}
