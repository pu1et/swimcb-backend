package com.project.swimcb.swimmingpool.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailFreeSwimming(
    @NonNull String imageUrl,
    @NonNull List<FreeSwimming> freeSwimming
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
