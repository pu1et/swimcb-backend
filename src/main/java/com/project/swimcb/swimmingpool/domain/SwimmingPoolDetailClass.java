package com.project.swimcb.swimmingpool.domain;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailClass(
    long swimmingClassId,
    @NonNull String type,
    @NonNull String subType,
    @NonNull LocalTime startTime,
    @NonNull LocalTime endTime,
    @NonNull List<String> days,
    @NonNull String time,
    int minimumPrice,
    boolean isReservable,
    boolean isFavorite
) {

}
