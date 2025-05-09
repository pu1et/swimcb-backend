package com.project.swimcb.bo.swimmingclass.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CancelBoSwimmingClassCommand(
    @NonNull Long swimmingPoolId,
    @NonNull Long swimmingClassId,
    @NonNull String cancelReason
) {

}
