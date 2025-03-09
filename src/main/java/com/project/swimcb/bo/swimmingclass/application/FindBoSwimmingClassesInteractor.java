package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse;
import com.project.swimcb.bo.swimmingclass.application.in.FindBoSwimmingClassesUseCase;
import com.project.swimcb.bo.swimmingclass.application.out.FindBoSwimmingClassesDsGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class FindBoSwimmingClassesInteractor implements FindBoSwimmingClassesUseCase {

  private final FindBoSwimmingClassesDsGateway gateway;

  @Override
  public FindBoSwimmingClassesResponse findBoSwimmingClasses(long swimmingPoolId, int month) {
    return gateway.findBySwimmingPoolId(swimmingPoolId, month);
  }
}
