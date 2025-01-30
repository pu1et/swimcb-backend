package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailClass(
    @NonNull String name,
    @NonNull String type,
    @NonNull String day,
    @NonNull String time,
    int price,
    boolean isClosed
) {

}
