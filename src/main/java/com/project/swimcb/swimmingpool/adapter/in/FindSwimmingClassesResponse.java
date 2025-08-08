package com.project.swimcb.swimmingpool.adapter.in;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public record FindSwimmingClassesResponse(
    @NonNull Page<SwimmingClass> classes
) {

  @Builder
  public record SwimmingClass(
      @NonNull Long swimmingPoolId,
      @NonNull String imageUrl,
      Long favoriteId,
      @NonNull Integer distance,
      @NonNull String name,
      @NonNull String address,
      @NonNull Double rating,
      @NonNull Integer reviewCount,
      @NonNull Integer minTicketPrice
  ) {

  }

}
