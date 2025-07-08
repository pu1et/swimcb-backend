package com.project.swimcb.bo.freeswimming.application;

import com.project.swimcb.bo.freeswimming.application.port.in.SetFreeSwimmingClosedUseCase;
import com.project.swimcb.bo.freeswimming.application.port.out.SetFreeSwimmingClosedDsGateway;
import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingClosedCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class SetFreeSwimmingClosedInteractor implements SetFreeSwimmingClosedUseCase {

  private final SetFreeSwimmingClosedDsGateway gateway;

  @Override
  public void setFreeSwimmingClosed(@NonNull SetFreeSwimmingClosedCommand command) {
    if (!gateway.existsAllByIdInAndSwimmingPoolId(
        command.freeSwimmingDayStatusIds(),
        command.swimmingPoolId()
    )) {
      throw new IllegalArgumentException("자유수영 일별 리스트가 존재하지 않습니다.");
    }

    gateway.setFreeSwimmingClosed(command);
  }

}
