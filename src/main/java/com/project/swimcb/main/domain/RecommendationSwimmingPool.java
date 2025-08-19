package com.project.swimcb.main.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record RecommendationSwimmingPool(
    @NonNull Long swimmingPoolId,
    @NonNull String imagePath,
    Long favoriteId,
    @NonNull Integer distance,
    @NonNull String name,
    @NonNull String address,
    @NonNull Double rating,
    @NonNull Integer reviewCount
) {

}
