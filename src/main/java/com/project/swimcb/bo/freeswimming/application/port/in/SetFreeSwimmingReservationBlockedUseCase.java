package com.project.swimcb.bo.freeswimming.application.port.in;

import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingReservationBlockedCommand;
import lombok.NonNull;

public interface SetFreeSwimmingReservationBlockedUseCase {

  void setFreeSwimmingReservationBlocked(@NonNull SetFreeSwimmingReservationBlockedCommand command);

}
