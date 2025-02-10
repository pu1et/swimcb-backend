package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;

public interface UpdateSwimmingPoolBasicInfoUseCase {

  void updateBasicInfo(long swimmingPoolId, UpdateSwimmingPoolBasicInfoCommand request);
}
