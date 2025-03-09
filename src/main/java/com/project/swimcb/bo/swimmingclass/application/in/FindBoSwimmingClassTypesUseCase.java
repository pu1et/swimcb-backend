package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse;

public interface FindBoSwimmingClassTypesUseCase {

  FindBoSwimmingClassTypesResponse findBoSwimmingClassTypes(long swimmingPoolId);
}
