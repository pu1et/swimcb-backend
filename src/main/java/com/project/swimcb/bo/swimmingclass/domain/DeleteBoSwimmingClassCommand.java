package com.project.swimcb.bo.swimmingclass.domain;

import lombok.Builder;

@Builder
public record DeleteBoSwimmingClassCommand(
    long swimmingPoolId,
    long swimmingClassId
) {

}
