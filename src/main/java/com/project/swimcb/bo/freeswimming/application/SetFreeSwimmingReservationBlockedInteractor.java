package com.project.swimcb.bo.freeswimming.application;

import com.project.swimcb.bo.freeswimming.application.port.in.SetFreeSwimmingReservationBlockedUseCase;
import com.project.swimcb.bo.freeswimming.application.port.out.SetFreeSwimmingReservationBlockedDsGateway;
import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingReservationBlockedCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class SetFreeSwimmingReservationBlockedInteractor implements
    SetFreeSwimmingReservationBlockedUseCase {

  private final SetFreeSwimmingReservationBlockedDsGateway gateway;

  @Override
  public void setFreeSwimmingReservationBlocked(@NonNull SetFreeSwimmingReservationBlockedCommand command) {
    if (!gateway.existsAllByIdInAndSwimmingPoolId(
        command.freeSwimmingDayStatusIds(),
        command.swimmingPoolId()
    )) {
      throw new IllegalArgumentException("자유수영 일별 리스트가 존재하지 않습니다.");
    }

    gateway.setFreeSwimmingReservationBlocked(command);
  }

}
