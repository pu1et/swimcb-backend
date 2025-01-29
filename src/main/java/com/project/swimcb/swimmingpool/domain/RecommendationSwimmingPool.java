package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record RecommendationSwimmingPool(
    @NonNull String imageUrl,
    boolean isFavorite,
    @NonNull String distance,
    @NonNull String name,
    @NonNull String address,
    @NonNull String star,
    int reviewCount
) {

}
