package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassesResponse;
import com.project.swimcb.swimmingpool.domain.FindSwimmingClassesCondition;
import lombok.NonNull;

public interface FindSwimmingClassesUseCase {

  FindSwimmingClassesResponse findSwimmingClasses(@NonNull FindSwimmingClassesCondition condition);
}
