package com.project.swimcb.swimmingpool.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindSwimmingPoolDetailMainResponse(
    @NonNull List<String> imageUrls,
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
