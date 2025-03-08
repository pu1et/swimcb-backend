package com.project.swimcb.bo.swimmingclass.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateBoSwimmingClassSubTypeCommand(
    long swimmingPoolId,
    long swimmingClassTypeId,
    @NonNull String name
) {

}
