package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFacilityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/facility")
public class FindSwimmingPoolDetailFacilityController {

  @Operation(summary = "수영장 상세 조회 - 시설정보")
  @GetMapping
  public FindSwimmingPoolDetailFacilityResponse findSwimmingPoolDetailFacility(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId
  ) {
    return FindSwimmingPoolDetailFacilityResponse.builder()
        .operatingDays("")
        .closedDays("")
        .newRegistrationPeriodStartDay(0)
        .newRegistrationPeriodEndDay(0)
        .reRegistrationPeriodStartDay(0)
        .reRegistrationPeriodEndDay(0)
        .build();
  }

}
