package com.project.swimcb.swimmingpool.adapter.in;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public record FindSwimmingClassesResponse(
    @NonNull Page<SwimmingClass> classes
) {

  @Builder
  public record SwimmingClass(
      long swimmingPoolId,
      @NonNull String imageUrl,
      boolean isFavorite,
      int distance,
      @NonNull String name,
      @NonNull String address,
      double rating,
      int reviewCount
  ) {

  }
}
