package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;

public interface FindSwimmingPoolDetailMainUseCase {

  SwimmingPoolDetailMain findSwimmingPoolDetailMain(long swimmingPoolId, Long memberId);
}
