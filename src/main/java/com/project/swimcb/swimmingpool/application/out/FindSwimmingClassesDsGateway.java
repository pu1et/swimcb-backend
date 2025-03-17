package com.project.swimcb.swimmingpool.application.out;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassesResponse;
import com.project.swimcb.swimmingpool.domain.FindSwimmingClassesCondition;
import lombok.NonNull;

public interface FindSwimmingClassesDsGateway {

  FindSwimmingClassesResponse findSwimmingClasses(@NonNull FindSwimmingClassesCondition condition);
}
