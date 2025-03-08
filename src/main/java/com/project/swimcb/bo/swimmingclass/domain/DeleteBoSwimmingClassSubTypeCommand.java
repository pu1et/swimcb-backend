package com.project.swimcb.bo.swimmingclass.domain;

import lombok.Builder;

@Builder
public record DeleteBoSwimmingClassSubTypeCommand(
    long swimmingPoolId,
    long swimmingClassTypeId,
    long swimmingClassSubTypeId
) {

}
