package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.ReserveSwimmingClassCommand;
import lombok.NonNull;

public interface ReserveSwimmingClassUseCase {

  void reserveSwimmingClass(@NonNull ReserveSwimmingClassCommand command);
}
