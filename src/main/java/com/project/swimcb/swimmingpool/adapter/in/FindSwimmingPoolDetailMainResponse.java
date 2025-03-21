package com.project.swimcb.swimmingpool.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindSwimmingPoolDetailMainResponse(
    @NonNull List<String> imageUrls,
    @NonNull String name,
    boolean isFavorite,
    double rating,
    int reviewCount,
    @NonNull String address,
    @NonNull String phone
) {

}
