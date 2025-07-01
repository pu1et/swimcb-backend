package com.project.swimcb.bo.freeswimming.application.port.in;

import com.project.swimcb.bo.freeswimming.domain.CreateBoFreeSwimmingCommand;
import lombok.NonNull;

public interface CreateBoFreeSwimmingUseCase {

  void createBoFreeSwimming(@NonNull CreateBoFreeSwimmingCommand command);
}
