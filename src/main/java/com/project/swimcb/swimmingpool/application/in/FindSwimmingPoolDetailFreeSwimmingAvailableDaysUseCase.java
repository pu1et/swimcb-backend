package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeAvailableDays;
import lombok.NonNull;

public interface FindSwimmingPoolDetailFreeSwimmingAvailableDaysUseCase {

  SwimmingPoolDetailFreeAvailableDays findSwimmingPoolDetailFreeSwimmingAvailableDays(
      @NonNull FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition condition);

}
