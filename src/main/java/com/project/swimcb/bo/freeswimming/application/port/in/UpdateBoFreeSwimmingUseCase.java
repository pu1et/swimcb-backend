package com.project.swimcb.bo.freeswimming.application.port.in;

import com.project.swimcb.bo.freeswimming.adapter.in.UpdateBoFreeSwimmingCommand;
import lombok.NonNull;

public interface UpdateBoFreeSwimmingUseCase {

  void updateFreeSwimmingImage(@NonNull UpdateBoFreeSwimmingCommand command);
}
