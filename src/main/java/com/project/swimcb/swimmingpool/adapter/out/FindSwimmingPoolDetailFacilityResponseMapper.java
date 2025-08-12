package com.project.swimcb.swimmingpool.adapter.out;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFacilityResponse;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFacility;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FindSwimmingPoolDetailFacilityResponseMapper {

  public FindSwimmingPoolDetailFacilityResponse toResponse(
      @NonNull SwimmingPoolDetailFacility facility
  ) {
    return FindSwimmingPoolDetailFacilityResponse.builder()
        .operatingDays(facility.operatingDays())
        .closedDays(facility.closedDays())
        .newRegistrationPeriodStartDay(facility.newRegistrationPeriodStartDay())
        .newRegistrationPeriodEndDay(facility.newRegistrationPeriodEndDay())
        .reRegistrationPeriodStartDay(facility.reRegistrationPeriodStartDay())
        .reRegistrationPeriodEndDay(facility.reRegistrationPeriodEndDay())
        .build();
  }
}
