package com.project.swimcb.reservation.domain;

import com.project.swimcb.db.entity.AccountNo;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReservationInfo(
    @NonNull SwimmingPool swimmingPool,
    @NonNull SwimmingClass swimmingClass,
    @NonNull Ticket ticket,
    @NonNull Reservation reservation,
    @NonNull Payment payment
) {

  @Builder
  public record SwimmingPool(
      long id,
      @NonNull String name,
      @NonNull AccountNo accountNo
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

  @Builder
  public record Reservation(
      long id,
      @NonNull LocalDateTime reservedAt,
      Integer waitingNo
  ) {

  }

  @Builder
  public record Payment(
      long id,
      @NonNull PaymentMethod method
  ) {

  }
}
