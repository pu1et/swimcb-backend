package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.domain.DeleteBoSwimmingClassCommand;
import lombok.NonNull;

public interface DeleteBoSwimmingClassUseCase {

  void deleteBoSwimmingClass(@NonNull DeleteBoSwimmingClassCommand request);
}
