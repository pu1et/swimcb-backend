package com.project.swimcb.swimmingpool.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailMain(
    @NonNull List<String> imageUrls,
    @NonNull String name,
    boolean isFavorite,
    @NonNull String star,
    int reviewCount,
    @NonNull String address,
    @NonNull String phoneNumber
) {

}
