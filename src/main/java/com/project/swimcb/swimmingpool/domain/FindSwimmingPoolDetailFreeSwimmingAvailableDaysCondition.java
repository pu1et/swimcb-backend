package com.project.swimcb.swimmingpool.domain;

import java.time.YearMonth;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition(
    @NonNull Long swimmingPoolId,
    @NonNull YearMonth month
) {

}
