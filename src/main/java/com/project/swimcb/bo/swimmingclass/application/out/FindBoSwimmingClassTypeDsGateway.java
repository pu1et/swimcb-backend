package com.project.swimcb.bo.swimmingclass.application.out;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse;

public interface FindBoSwimmingClassTypeDsGateway {

  FindBoSwimmingClassTypesResponse findBoSwimmingClassTypes(long swimmingPoolId);
}
