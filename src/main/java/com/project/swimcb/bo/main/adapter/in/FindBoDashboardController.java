package com.project.swimcb.bo.main.adapter.in;

import com.project.swimcb.bo.main.adapter.in.FindBoDashboardResponse.DailyReservationStatus;
import com.project.swimcb.bo.main.adapter.in.FindBoDashboardResponse.MonthlyReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/dashboard")
@RequiredArgsConstructor
public class FindBoDashboardController {

  @Operation(summary = "대시보드")
  @GetMapping
  public FindBoDashboardResponse findBoDashboard() {
    return FindBoDashboardResponse.builder()
        .reservationRequestCount(3)
        .cancellationProcessedCount(1)
        .refundProcessedCount(1)
        .monthlyReservationStatus(
            List.of(
                MonthlyReservationStatus.builder()
                    .week(1)
                    .swimmingClassReservationCount(20)
                    .freeSwimmingReservationCount(0)
                    .build(),
                MonthlyReservationStatus.builder()
                    .week(2)
                    .swimmingClassReservationCount(10)
                    .freeSwimmingReservationCount(5)
                    .build(),
                MonthlyReservationStatus.builder()
                    .week(3)
                    .swimmingClassReservationCount(30)
                    .freeSwimmingReservationCount(0)
                    .build(),
                MonthlyReservationStatus.builder()
                    .week(4)
                    .swimmingClassReservationCount(30)
                    .freeSwimmingReservationCount(20)
                    .build(),
                MonthlyReservationStatus.builder()
                    .week(5)
                    .swimmingClassReservationCount(0)
                    .freeSwimmingReservationCount(5)
                    .build()
            )
        )
        .dailyReservationStatus(
            List.of(
                DailyReservationStatus.builder()
                    .date(LocalDate.of(2024, 11, 23))
                    .paymentCount(10)
                    .paymentAmount(15300)
                    .cancelCount(0)
                    .refundCount(0)
                    .build(),
                DailyReservationStatus.builder()
                    .date(LocalDate.of(2024, 11, 24))
                    .paymentCount(9)
                    .paymentAmount(229000)
                    .cancelCount(0)
                    .refundCount(0)
                    .build(),
                DailyReservationStatus.builder()
                    .date(LocalDate.of(2024, 11, 25))
                    .paymentCount(1)
                    .paymentAmount(90000)
                    .cancelCount(0)
                    .refundCount(0)
                    .build(),
                DailyReservationStatus.builder()
                    .date(LocalDate.of(2024, 11, 26))
                    .paymentCount(3)
                    .paymentAmount(104000)
                    .cancelCount(10000)
                    .refundCount(10000)
                    .build()
            )
        ).build();
  }
}
