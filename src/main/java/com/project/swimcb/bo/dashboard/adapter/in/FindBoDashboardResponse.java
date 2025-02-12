package com.project.swimcb.bo.dashboard.adapter.in;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoDashboardResponse(
    int reservationRequestCount,
    int cancellationProcessedCount,
    int refundProcessedCount,
    @NonNull List<MonthlyReservationStatus> monthlyReservationStatus,
    @NonNull List<DailyReservationStatus> dailyReservationStatus
    ) {

  @Builder
  record MonthlyReservationStatus(
      int week,
      int swimmingClassReservationCount,
      int freeSwimmingReservationCount
  ) {

  }

  @Builder
  record DailyReservationStatus(
      @NonNull LocalDate date,
      int paymentCount,
      int paymentAmount,
      int cancelCount,
      int refundCount
  ) {

  }
}
