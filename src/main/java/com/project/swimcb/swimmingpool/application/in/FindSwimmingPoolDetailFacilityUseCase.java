package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFacility;
import lombok.NonNull;

public interface FindSwimmingPoolDetailFacilityUseCase {

  SwimmingPoolDetailFacility findSwimmingPoolDetailFacility(@NonNull Long swimmingPoolId);

}
