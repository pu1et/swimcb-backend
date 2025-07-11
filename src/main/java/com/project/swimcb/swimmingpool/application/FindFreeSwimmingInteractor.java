package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.swimmingpool.application.in.FindFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.application.in.FindFreeSwimmingUseCase;
import com.project.swimcb.swimmingpool.application.out.FindFreeSwimmingDsGateway;
import com.project.swimcb.swimmingpool.domain.FreeSwimming;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindFreeSwimmingInteractor implements FindFreeSwimmingUseCase {

  private final FindFreeSwimmingDsGateway gateway;

  @Override
  public List<FreeSwimming> findFreeSwimming(@NonNull FindFreeSwimmingCondition condition) {
    return gateway.findFreeSwimming(condition);
  }

}
