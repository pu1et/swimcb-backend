package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesCondition;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailClassesUseCase;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailClassesGateway;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindSwimmingPoolDetailClassesInteractor implements FindSwimmingPoolDetailClassesUseCase {

  private final FindSwimmingPoolDetailClassesGateway gateway;

  @Override
  public FindSwimmingPoolDetailClassesResponse findSwimmingPoolDetailClasses(
      @NonNull FindSwimmingPoolDetailClassesCondition condition) {
    return gateway.findSwimmingPoolDetailClasses(condition);
  }
}
