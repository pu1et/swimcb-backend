package com.project.swimcb.bo.freeswimming.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CancelBoFreeSwimmingCommand(
    @NonNull Long swimmingPoolId,
    @NonNull Long freeSwimmingId,
    @NonNull String cancelReason
) {

}
