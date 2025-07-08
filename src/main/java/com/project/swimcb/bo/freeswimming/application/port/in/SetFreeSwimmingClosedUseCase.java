package com.project.swimcb.bo.freeswimming.application.port.in;

import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingClosedCommand;
import lombok.NonNull;

public interface SetFreeSwimmingClosedUseCase {

  void setFreeSwimmingClosed(@NonNull SetFreeSwimmingClosedCommand command);

}
