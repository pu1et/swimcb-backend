package com.project.swimcb.bo.freeswimming.application;

import com.project.swimcb.bo.freeswimming.application.port.in.FindBoFreeSwimmingUseCase;
import com.project.swimcb.bo.freeswimming.application.port.out.FindBoFreeSwimmingDsGateway;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming;
import java.time.LocalDate;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindBoFreeSwimmingInteractor implements FindBoFreeSwimmingUseCase {

  private final FindBoFreeSwimmingDsGateway gateway;

  @Override
  public List<BoFreeSwimming> findBoFreeSwimming(
      @NonNull Long swimmingPoolId,
      @NonNull LocalDate yearMonth
  ) {
    return gateway.findBoFreeSwimming(swimmingPoolId, yearMonth);
  }

}
