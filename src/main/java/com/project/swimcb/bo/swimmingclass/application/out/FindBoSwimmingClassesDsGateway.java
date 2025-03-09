package com.project.swimcb.bo.swimmingclass.application.out;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse;

public interface FindBoSwimmingClassesDsGateway {

  FindBoSwimmingClassesResponse findBySwimmingPoolId(long swimmingPoolId, int month);
}
