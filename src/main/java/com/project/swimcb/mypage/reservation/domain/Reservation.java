package com.project.swimcb.mypage.reservation.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Reservation(
    long reservationId,
    @NonNull LocalDate date,
    @NonNull String status,
    String waitingNumberFlag,
    @NonNull SwimmingPool swimmingPool,
    @NonNull Swimming swimming
) {

  @Builder
  public record SwimmingPool(
      @NonNull String imageUrl,
      @NonNull String name
  ) {

  }

  @Builder
  public record Swimming(
      @NonNull String type,
      int price
  ) {

  }
}
