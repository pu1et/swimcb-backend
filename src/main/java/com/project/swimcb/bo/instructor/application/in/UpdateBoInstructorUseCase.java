package com.project.swimcb.bo.instructor.application.in;

import com.project.swimcb.bo.instructor.domain.UpdateBoInstructorCommand;
import jakarta.validation.constraints.NotNull;

public interface UpdateBoInstructorUseCase {

  void updateBoInstructor(@NotNull UpdateBoInstructorCommand request);
}
