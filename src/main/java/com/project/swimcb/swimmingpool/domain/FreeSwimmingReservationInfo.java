package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FreeSwimmingReservationInfo(
    @NonNull SwimmingPool swimmingPool,
    @NonNull FreeSwimming freeSwimming
) {

  @Builder
  public record SwimmingPool(
      @NonNull String name,
      @NonNull String address
  ) {

  }

  @Builder
  public record FreeSwimming(
      @NonNull String name,
      @NonNull String dateTime,
      int price,
      @NonNull String consentAgreement
  ) {

  }
}
