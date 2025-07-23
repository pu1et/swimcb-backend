package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFreeSwimmingUseCase;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeSwimmingDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindSwimmingPoolDetailFreeSwimmingInteractor implements
    FindSwimmingPoolDetailFreeSwimmingUseCase {

  private final FindSwimmingPoolDetailFreeSwimmingDsGateway gateway;

  @Override
  public SwimmingPoolDetailFreeSwimming findSwimmingPoolDetailFreeSwimming(
      @NonNull FindSwimmingPoolDetailFreeSwimmingCondition condition) {
    return gateway.findSwimmingPoolDetailFreeSwimming(condition);
  }

}
