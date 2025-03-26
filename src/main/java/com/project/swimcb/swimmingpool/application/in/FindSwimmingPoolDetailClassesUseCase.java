package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesCondition;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse;
import lombok.NonNull;

public interface FindSwimmingPoolDetailClassesUseCase {

  FindSwimmingPoolDetailClassesResponse findSwimmingPoolDetailClasses(
      @NonNull FindSwimmingPoolDetailClassesCondition condition);
}
