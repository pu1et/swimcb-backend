package com.project.swimcb.bo.freeswimming.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SetFreeSwimmingReservationBlockedCommand(
    @NonNull Long swimmingPoolId,
    @NonNull List<Long> freeSwimmingDayStatusIds,
    @NonNull Boolean isReservationBlocked
) {

}
