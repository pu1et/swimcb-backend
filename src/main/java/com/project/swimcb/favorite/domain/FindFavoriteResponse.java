package com.project.swimcb.favorite.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindFavoriteResponse(
    @NonNull List<Favorite> favorites
) {

  @Builder
  public record Favorite(
      long favoriteId,
      long swimmingPoolId,
      @NonNull String imageUrl,
      @NonNull String distance,
      @NonNull String flag,
      @NonNull String registrationInfo,
      boolean isFavorite,
      @NonNull String name,
      @NonNull String address,
      @NonNull String star,
      int reviewCount
  ) {

  }
}
