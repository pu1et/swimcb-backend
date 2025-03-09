package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse;

public interface FindBoSwimmingClassesUseCase {

  FindBoSwimmingClassesResponse findBoSwimmingClasses(long swimmingPoolId, int month);
}
