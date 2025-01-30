package com.project.swimcb.mypage.reservation.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReservationDetail(
    @NonNull Reservation reservation,
    @NonNull SwimmingPool swimmingPool,
    SwimmingClass swimmingClass,
    FreeSwimming freeSwimming,
    @NonNull List<Payment> payments
) {

  @Builder
  public record Reservation(
      String waitingNumberFlag,
      @NonNull LocalDateTime dateTime
  ) {

  }

  @Builder
  public record SwimmingPool(
      @NonNull String name,
      @NonNull String address
  ) {

  }

  @Builder
  public record SwimmingClass(
      @NonNull String type,
      @NonNull String subType,
      @NonNull String date,
      @NonNull String time,
      int price
  ) {

  }

  @Builder
  public record FreeSwimming(
      @NonNull String type,
      int price
  ) {

  }

  @Builder
  public record Payment(
      @NonNull String method,
      int price
  ) {

  }
}
