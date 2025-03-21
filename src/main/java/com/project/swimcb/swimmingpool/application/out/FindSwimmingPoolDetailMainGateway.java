package com.project.swimcb.swimmingpool.application.out;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;

public interface FindSwimmingPoolDetailMainGateway {

  SwimmingPoolDetailMain findSwimmingPoolDetailMain(long swimmingPoolId, Long memberId);
}
