package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingClass(
    @NonNull String imageUrl,
    boolean isFavorite,
    @NonNull String distance,
    @NonNull String name,
    @NonNull String address,
    int lowestPrice,
    @NonNull String star,
    int reviewCount
) {

}
