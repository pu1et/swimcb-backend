package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingClassReservationInfo(
    @NonNull SwimmingPool swimmingPool,
    @NonNull SwimmingClass swimmingClass
) {

  @Builder
  public record SwimmingPool(
      @NonNull String name,
      @NonNull String address
  ) {

  }

  @Builder
  public record SwimmingClass(
      @NonNull String type,
      @NonNull String day,
      @NonNull String time,
      int price,
      @NonNull String consentAgreement
  ) {

  }
}
