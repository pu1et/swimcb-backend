package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FreeSwimming(
    @NonNull Long swimmingPoolId,
    @NonNull String imagePath,
    @NonNull Boolean isFavorite,
    @NonNull Integer distance,
    @NonNull String name,
    @NonNull String address,
    @NonNull Double rating,
    @NonNull Integer reviewCount
) {

}
