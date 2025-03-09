package com.project.swimcb.bo.swimmingclass.application.out;

import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import lombok.NonNull;

public interface UpdateBoSwimmingClassDsGateway {

  void updateSwimmingClass(@NonNull UpdateBoSwimmingClassCommand request);
}
