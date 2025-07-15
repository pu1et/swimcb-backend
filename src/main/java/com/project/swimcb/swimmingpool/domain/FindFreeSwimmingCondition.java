package com.project.swimcb.swimmingpool.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindFreeSwimmingCondition(
    Long memberId,

    @NonNull Double memberLatitude,
    @NonNull Double memberLongitude,
    @NonNull Double minLatitude,
    @NonNull Double maxLatitude,
    @NonNull Double minLongitude,
    @NonNull Double maxLongitude,

    @NonNull Boolean isTodayAvailable,
    LocalDate date,
    @NonNull List<LocalTime> startTimes
) {

}
