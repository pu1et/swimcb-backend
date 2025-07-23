package com.project.swimcb.swimmingpool.application.out;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
import lombok.NonNull;

public interface FindSwimmingPoolDetailFreeSwimmingDsGateway {

  SwimmingPoolDetailFreeSwimming findSwimmingPoolDetailFreeSwimming(
      @NonNull FindSwimmingPoolDetailFreeSwimmingCondition condition);
}
