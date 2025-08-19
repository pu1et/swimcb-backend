package com.project.swimcb.main.domain;

import lombok.NonNull;

public record RecommendationSwimmingPool(
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
