package com.project.swimcb.swimmingpool.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindRecommendationSwimmingPoolResponse(
    @NonNull List<SwimmingPool> swimmingPools
) {

  @Builder
  public record SwimmingPool(
      @NonNull Long swimmingPoolId,
      @NonNull String imageUrl,
      Long favoriteId,
      @NonNull Integer distance,
      @NonNull String name,
      @NonNull String address,
      @NonNull Double rating,
      @NonNull Integer reviewCount
  ) {

  }

}
