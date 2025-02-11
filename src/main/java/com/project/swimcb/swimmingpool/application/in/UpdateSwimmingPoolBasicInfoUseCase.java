package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import lombok.NonNull;

public interface UpdateSwimmingPoolBasicInfoUseCase {

  void updateBasicInfo(long swimmingPoolId, @NonNull UpdateSwimmingPoolBasicInfoCommand request);
}
