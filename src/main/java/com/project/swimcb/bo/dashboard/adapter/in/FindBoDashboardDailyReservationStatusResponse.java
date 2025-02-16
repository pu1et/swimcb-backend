package com.project.swimcb.bo.dashboard.adapter.in;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Page;

@Builder
public record FindBoDashboardDailyReservationStatusResponse(
    @NonNull ReservationSummary reservationSummary,
    @NonNull Page<EachDayReservationStatus> eachDayReservationStatus
) {

  @Builder
  record ReservationSummary(
      int totalPaymentCount,
      long totalPaymentAmount,
      int totalCancelCount,
      int totalRefundCount
  ) {

  }

  @Builder
  record EachDayReservationStatus(
      @NonNull LocalDate date,
      int paymentCount,
      int paymentAmount,
      int cancelCount,
      int refundCount
  ) {

  }
}
