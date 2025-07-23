package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFreeSwimmingDetailUseCase;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeSwimmingDetailDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingDetailCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindSwimmingPoolDetailFreeSwimmingDetailInteractor implements
    FindSwimmingPoolDetailFreeSwimmingDetailUseCase {

  private final FindSwimmingPoolDetailFreeSwimmingDetailDsGateway gateway;

  @Override
  public SwimmingPoolDetailFreeSwimmingDetail findSwimmingPoolDetailFreeSwimmingDetail(
      @NonNull FindSwimmingPoolDetailFreeSwimmingDetailCondition condition) {
    return gateway.findSwimmingPoolDetailFreeSwimmingDetail(condition);
  }

}
