package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse;
import com.project.swimcb.bo.swimmingclass.application.in.FindBoSwimmingClassTypesUseCase;
import com.project.swimcb.bo.swimmingclass.application.out.FindBoSwimmingClassTypeDsGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindBoSwimmingClassTypesInteractor implements FindBoSwimmingClassTypesUseCase {

  private final FindBoSwimmingClassTypeDsGateway gateway;

  @Override
  public FindBoSwimmingClassTypesResponse findBoSwimmingClassTypes(long swimmingPoolId) {
    return gateway.findBoSwimmingClassTypes(swimmingPoolId);
  }
}
