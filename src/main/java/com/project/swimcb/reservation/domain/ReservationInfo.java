package com.project.swimcb.reservation.domain;

import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import java.time.LocalTime;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReservationInfo(
    @NonNull SwimmingPool swimmingPool,
    @NonNull SwimmingClass swimmingClass,
    @NonNull Ticket ticket,
    @NonNull PaymentMethod paymentMethod
) {

  @Builder
  public record SwimmingPool(
      long id,
      @NonNull String name
  ) {

  }

  @Builder
  public record SwimmingClass(
      long id,
      int month,
      @NonNull SwimmingClassTypeName type,
      @NonNull String subType,
      int daysOfWeek,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  public record Ticket(
      long id,
      @NonNull String name,
      int price
  ) {

  }
}
