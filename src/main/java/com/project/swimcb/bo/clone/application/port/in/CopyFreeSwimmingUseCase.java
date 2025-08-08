package com.project.swimcb.bo.clone.application.port.in;

import com.project.swimcb.bo.clone.domain.CopyFreeSwimmingCommand;
import lombok.NonNull;

public interface CopyFreeSwimmingUseCase {

  void copyFreeSwimming(@NonNull CopyFreeSwimmingCommand command);

}
