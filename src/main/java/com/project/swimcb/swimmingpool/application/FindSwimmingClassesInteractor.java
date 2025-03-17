package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassesResponse;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingClassesUseCase;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingClassesDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingClassesCondition;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindSwimmingClassesInteractor implements FindSwimmingClassesUseCase {

  private final FindSwimmingClassesDsGateway gateway;

  @Override
  public FindSwimmingClassesResponse findSwimmingClasses(@NonNull FindSwimmingClassesCondition condition) {
    return gateway.findSwimmingClasses(condition);
  }
}
