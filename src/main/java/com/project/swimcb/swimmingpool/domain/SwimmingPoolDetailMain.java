package com.project.swimcb.swimmingpool.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailMain(
    @NonNull List<String> imagePaths,
    @NonNull String name,
    Long favoriteId,
    double rating,
    int reviewCount,
    @NonNull String address,
    @NonNull String phone,
    Double latitude,
    Double longitude
) {

}
