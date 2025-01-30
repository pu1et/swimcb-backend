package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailClass(
    @NonNull String name,
    @NonNull String classType,
    @NonNull String classDay,
    @NonNull String classTime,
    int price,
    boolean isClassClosed
) {

}
