package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailFreeSwimming(
    @NonNull String imageUrl,
    @NonNull FreeSwimming freeSwimming
) {

  @Builder
  public record FreeSwimming(
      long freeSwimmingId,
      @NonNull String name,
      int price,
      boolean isReservable
  ) {

  }
}
