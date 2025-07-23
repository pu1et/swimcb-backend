package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
import lombok.NonNull;

public interface FindSwimmingPoolDetailFreeSwimmingUseCase {

  SwimmingPoolDetailFreeSwimming findSwimmingPoolDetailFreeSwimming(
      @NonNull FindSwimmingPoolDetailFreeSwimmingCondition condition);

}
