package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;

@Builder
public record FindSwimmingPoolDetailFacilityResponse(
    String operatingDays,
    String closedDays,
    Integer newRegistrationPeriodStartDay,
    Integer newRegistrationPeriodEndDay,
    Integer reRegistrationPeriodStartDay,
    Integer reRegistrationPeriodEndDay
) {

}
