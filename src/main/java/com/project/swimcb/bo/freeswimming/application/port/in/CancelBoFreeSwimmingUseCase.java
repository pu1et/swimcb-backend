package com.project.swimcb.bo.freeswimming.application.port.in;

import com.project.swimcb.bo.freeswimming.domain.CancelBoFreeSwimmingCommand;
import lombok.NonNull;

public interface CancelBoFreeSwimmingUseCase {

  void cancelBoFreeSwimming(@NonNull CancelBoFreeSwimmingCommand command);

}
