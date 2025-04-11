package com.project.swimcb.mypage.reservation.domain;

import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReservationDetail(
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
      @NonNull String imagePath
  ) {

  }

  @Builder
  public record SwimmingClass(
      long id,
      @NonNull SwimmingClassTypeName type,
      @NonNull String subType
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
      @NonNull ReservationStatus status,
      @NonNull LocalDateTime reservedAt
  ) {

  }

  @Builder
  public record Payment(
      @NonNull PaymentMethod method,
      int amount
  ) {

  }
}
