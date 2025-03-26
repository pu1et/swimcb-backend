package com.project.swimcb.swimmingpool.application.out;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesCondition;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse;
import lombok.NonNull;

public interface FindSwimmingPoolDetailClassesGateway {

  FindSwimmingPoolDetailClassesResponse findSwimmingPoolDetailClasses(@NonNull
      FindSwimmingPoolDetailClassesCondition condition);
}
