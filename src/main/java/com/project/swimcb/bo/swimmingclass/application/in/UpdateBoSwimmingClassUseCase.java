package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import lombok.NonNull;

public interface UpdateBoSwimmingClassUseCase {

  void updateBoSwimmingClass(@NonNull UpdateBoSwimmingClassCommand request);
}
