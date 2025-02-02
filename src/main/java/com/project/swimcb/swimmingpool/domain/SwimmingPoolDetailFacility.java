package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailFacility(
    @NonNull String openingHours,
    @NonNull String size,
    @NonNull String depth
) {

}
