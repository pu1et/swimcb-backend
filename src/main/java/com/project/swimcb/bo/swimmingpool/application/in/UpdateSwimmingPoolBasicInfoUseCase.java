package com.project.swimcb.bo.swimmingpool.application.in;

import com.project.swimcb.bo.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import lombok.NonNull;

public interface UpdateSwimmingPoolBasicInfoUseCase {

  void updateBasicInfo(long swimmingPoolId, @NonNull UpdateSwimmingPoolBasicInfoCommand request);
}
