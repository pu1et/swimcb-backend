package com.project.swimcb.swimmingpool.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindSwimmingPoolDetailFreeSwimmingDetailCondition(
    Long memberId,
    @NonNull Long swimmingPoolId,
    @NonNull LocalDate date
) {

}
