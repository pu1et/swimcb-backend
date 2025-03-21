package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailMainUseCase;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailMainGateway;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindSwimmingPoolDetailMainInteractor implements FindSwimmingPoolDetailMainUseCase {

  private final FindSwimmingPoolDetailMainGateway gateway;

  @Override
  public SwimmingPoolDetailMain findSwimmingPoolDetailMain(long swimmingPoolId, Long memberId) {
    return gateway.findSwimmingPoolDetailMain(swimmingPoolId, memberId);
  }
}
