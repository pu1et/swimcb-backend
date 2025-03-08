package com.project.swimcb.bo.instructor.application.in;

import com.project.swimcb.bo.instructor.domain.CreateBoInstructorCommand;
import jakarta.validation.constraints.NotNull;

public interface CreateBoInstructorUseCase {

  void createBoInstructor(@NotNull CreateBoInstructorCommand request);
}
