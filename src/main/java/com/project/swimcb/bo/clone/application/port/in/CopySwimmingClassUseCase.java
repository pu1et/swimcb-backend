package com.project.swimcb.bo.clone.application.port.in;

import com.project.swimcb.bo.clone.domain.CopySwimmingClassCommand;
import lombok.NonNull;

public interface CopySwimmingClassUseCase {

  void copySwimmingClass(@NonNull CopySwimmingClassCommand command);
}
