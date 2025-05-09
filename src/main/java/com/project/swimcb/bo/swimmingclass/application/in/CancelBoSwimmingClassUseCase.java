package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.domain.CancelBoSwimmingClassCommand;
import lombok.NonNull;

public interface CancelBoSwimmingClassUseCase {

  void cancelBoSwimmingClass(@NonNull CancelBoSwimmingClassCommand request);
}
