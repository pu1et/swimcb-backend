package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FreeSwimming(
    @NonNull Long swimmingPoolId,
    @NonNull String imagePath,
    Long favoriteId,
    @NonNull Integer distance,
    @NonNull String name,
    @NonNull String address,
    @NonNull Double rating,
    @NonNull Integer reviewCount,
    @NonNull Double latitude,
    @NonNull Double longitude
) {

}
