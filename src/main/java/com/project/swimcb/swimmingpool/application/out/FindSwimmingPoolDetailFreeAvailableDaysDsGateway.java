package com.project.swimcb.swimmingpool.application.out;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeAvailableDays;
import lombok.NonNull;

public interface FindSwimmingPoolDetailFreeAvailableDaysDsGateway {

  SwimmingPoolDetailFreeAvailableDays findSwimmingPoolDetailFreeAvailableDays(
      @NonNull FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition condition);

}
