package com.project.swimcb.bo.dashboard.adapter.in;

import com.project.swimcb.bo.dashboard.adapter.in.FindBoDashboardMonthlyReservationStatusResponse.WeeklyReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/dashboard/monthly-reservation-status")
@RequiredArgsConstructor
public class FindBoDashboardMonthlyReservationStatusController {

  @Operation(summary = "대시보드 - 월 예약 현황")
  @GetMapping
  public FindBoDashboardMonthlyReservationStatusResponse findBoDashboardMonthlyReservationStatus() {
    return new FindBoDashboardMonthlyReservationStatusResponse(
        List.of(
            WeeklyReservationStatus.builder()
                .week(1)
                .swimmingClassReservationCount(20)
                .freeSwimmingReservationCount(0)
                .build(),
            WeeklyReservationStatus.builder()
                .week(2)
                .swimmingClassReservationCount(10)
                .freeSwimmingReservationCount(5)
                .build(),
            WeeklyReservationStatus.builder()
                .week(3)
                .swimmingClassReservationCount(30)
                .freeSwimmingReservationCount(0)
                .build(),
            WeeklyReservationStatus.builder()
                .week(4)
                .swimmingClassReservationCount(30)
                .freeSwimmingReservationCount(20)
                .build(),
            WeeklyReservationStatus.builder()
                .week(5)
                .swimmingClassReservationCount(0)
                .freeSwimmingReservationCount(5)
                .build()
        )
    );
  }
}
