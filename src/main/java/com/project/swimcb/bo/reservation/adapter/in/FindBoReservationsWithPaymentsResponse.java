package com.project.swimcb.bo.reservation.adapter.in;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoReservationsWithPaymentsResponse(
    @NonNull String reservationPaymentStatus,
    @NonNull String customerName,
    @NonNull LocalDate birthdate,
    @NonNull SwimmingClass swimmingClass,
    @NonNull LocalDate reservationDate,
    @NonNull Payment payment
) {

  @Builder
  record SwimmingClass(
      @NonNull String type,
      @NonNull String category,
      @NonNull LocalTime startTime,
      @NonNull List<String> days
  ) {

  }

  @Builder
  record Payment(
      LocalDate date,
      @NonNull String method,
      int amount,
      boolean isApproved
  ) {

  }
}
