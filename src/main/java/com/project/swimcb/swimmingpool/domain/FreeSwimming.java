package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;

@Builder
public record FreeSwimming(
    long swimmingPoolId,
    double latitude,
    double longitude
) {

}
