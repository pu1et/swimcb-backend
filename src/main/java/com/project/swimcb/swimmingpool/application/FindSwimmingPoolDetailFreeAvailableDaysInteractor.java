package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeAvailableDaysDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFreeSwimmingAvailableDaysUseCase;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeAvailableDays;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindSwimmingPoolDetailFreeAvailableDaysInteractor implements
    FindSwimmingPoolDetailFreeSwimmingAvailableDaysUseCase {

  private final FindSwimmingPoolDetailFreeAvailableDaysDsGateway gateway;

  @Override
  public SwimmingPoolDetailFreeAvailableDays findSwimmingPoolDetailFreeSwimmingAvailableDays(
      @NonNull FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition condition) {
    return gateway.findSwimmingPoolDetailFreeAvailableDays(condition);
  }

}
