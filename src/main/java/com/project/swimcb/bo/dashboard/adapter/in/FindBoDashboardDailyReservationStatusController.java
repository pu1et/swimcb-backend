package com.project.swimcb.bo.dashboard.adapter.in;

import com.project.swimcb.bo.dashboard.adapter.in.FindBoDashboardDailyReservationStatusResponse.EachDayReservationStatus;
import com.project.swimcb.bo.dashboard.adapter.in.FindBoDashboardDailyReservationStatusResponse.ReservationSummary;
import com.project.swimcb.bo.dashboard.domain.enums.BoDashboardDailyReservationStatusSwimmingType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/dashboard/daily-reservation-status")
@RequiredArgsConstructor
public class FindBoDashboardDailyReservationStatusController {

  @Operation(summary = "대시보드 - 일일 예약 현황")
  @GetMapping
  public FindBoDashboardDailyReservationStatusResponse findBoDashboardDailyReservationStatus(
      @Parameter(description = "조회 타입") @RequestParam(value = "type", required = false) BoDashboardDailyReservationStatusSwimmingType type,
      @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
      @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return FindBoDashboardDailyReservationStatusResponse.builder()
        .reservationSummary(
            ReservationSummary.builder()
                .totalPaymentAmount(219)
                .totalPaymentCount(12240000)
                .totalCancelCount(0)
                .totalRefundCount(0)
                .build()
        )
        .eachDayReservationStatus(
            new PageImpl<>(List.of(
                EachDayReservationStatus.builder()
                    .date(LocalDate.of(2024, 11, 23))
                    .paymentCount(10)
                    .paymentAmount(15300)
                    .cancelCount(0)
                    .refundCount(0)
                    .build(),
                EachDayReservationStatus.builder()
                    .date(LocalDate.of(2024, 11, 24))
                    .paymentCount(9)
                    .paymentAmount(229000)
                    .cancelCount(0)
                    .refundCount(0)
                    .build(),
                EachDayReservationStatus.builder()
                    .date(LocalDate.of(2024, 11, 25))
                    .paymentCount(1)
                    .paymentAmount(90000)
                    .cancelCount(0)
                    .refundCount(0)
                    .build(),
                EachDayReservationStatus.builder()
                    .date(LocalDate.of(2024, 11, 26))
                    .paymentCount(3)
                    .paymentAmount(104000)
                    .cancelCount(10000)
                    .refundCount(10000)
                    .build()
            ))).build();
  }
}
