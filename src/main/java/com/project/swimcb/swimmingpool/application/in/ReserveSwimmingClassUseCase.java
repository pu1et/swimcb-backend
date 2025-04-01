package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.ReservationInfo;
import com.project.swimcb.swimmingpool.domain.ReserveSwimmingClassCommand;
import lombok.NonNull;

public interface ReserveSwimmingClassUseCase {

  ReservationInfo reserveSwimmingClass(@NonNull ReserveSwimmingClassCommand command);
}
