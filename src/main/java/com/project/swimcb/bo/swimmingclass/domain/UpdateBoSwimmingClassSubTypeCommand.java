package com.project.swimcb.bo.swimmingclass.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateBoSwimmingClassSubTypeCommand(
    long swimmingPoolId,
    long swimmingClassTypeId,
    long swimmingClassSubTypeId,
    @NonNull String name
) {

}
