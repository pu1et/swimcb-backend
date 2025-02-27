package com.project.swimcb.bo.swimmingpool.application.in;

import com.project.swimcb.bo.swimmingpool.adapter.in.FindSwimmingPoolBasicInfoResponse;

public interface FindSwimmingPoolBasicInfoUseCase {

  FindSwimmingPoolBasicInfoResponse findSwimmingPoolBasicInfo(long swimmingPoolId);
}
